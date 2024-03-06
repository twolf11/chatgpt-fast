package com.aigcfast.chat.service.strategy.user;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import com.aigcfast.chat.util.WebUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cn.dev33.satoken.stp.SaLoginConfig;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.MD5;
import com.aigcfast.chat.common.enums.EmailBizTypeEnum;
import com.aigcfast.chat.common.enums.UserRegisterTypeEnum;
import com.aigcfast.chat.common.exception.ServiceException;
import com.aigcfast.chat.domain.convert.UserConvert;
import com.aigcfast.chat.domain.entity.sys.SysUser;
import com.aigcfast.chat.domain.entity.sys.SysUserEmail;
import com.aigcfast.chat.domain.entity.sys.SysUserInvite;
import com.aigcfast.chat.domain.request.sys.UserLoginRequest;
import com.aigcfast.chat.domain.request.sys.UserRegisterRequest;
import com.aigcfast.chat.domain.vo.sys.UserInfoVO;
import com.aigcfast.chat.domain.vo.sys.UserLoginVO;
import com.aigcfast.chat.service.common.EmailService;
import com.aigcfast.chat.service.log.LogUserLoginService;
import com.aigcfast.chat.service.sys.SysUserEmailService;
import com.aigcfast.chat.service.sys.SysUserInviteService;
import com.aigcfast.chat.service.sys.SysUserService;
import com.aigcfast.chat.util.AssertUtil;
import com.aigcfast.chat.util.Tools;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.aigcfast.chat.common.constant.RedisConstant.REGISTER_VERIFY_CODE;
import static com.aigcfast.chat.common.constant.RedisConstant.SEND_EMAIL_LIMIT;
import static com.aigcfast.chat.common.constant.UserConstant.JWT_USER_INFO;

/**
 * 邮箱用户策略
 * @Author lcy
 * @Date 2023/7/12 17:43
 */
@Component
@AllArgsConstructor
@Slf4j
public class UserEmailStrategy extends AbstractUserTypeStrategy {

    protected SysUserService userService;

    protected SysUserEmailService userEmailService;

    protected EmailService emailService;

    private RedisTemplate<String,String> redisTemplate;

    private ThreadPoolTaskExecutor executor;

    private SysUserInviteService userInviteService;

    private LogUserLoginService userLoginService;

    @Override public boolean identityCanUsed(String identity){
        SysUserEmail byEmail = userEmailService.getByEmail(identity);
        return ObjectUtil.isNull(byEmail);
    }

    @Override public void sendRegisterVerifyCode(String identity){
        String redisKey = String.format(REGISTER_VERIFY_CODE,identity);
        String limitKey = String.format(SEND_EMAIL_LIMIT,identity);
        Boolean success = redisTemplate.opsForValue().setIfAbsent(redisKey,limitKey,1,TimeUnit.MINUTES);
        AssertUtil.isTrue(Boolean.FALSE.equals(success),"一分钟以内不能重复发送");

        String verifyCode = RandomUtil.randomNumbers(6);
        success = redisTemplate.opsForValue().setIfAbsent(redisKey,verifyCode,10,TimeUnit.MINUTES);
        if (Boolean.FALSE.equals(success)) {
            verifyCode = redisTemplate.opsForValue().get(redisKey);
        }
        // 发送邮箱验证信息
        emailService.sendEmail(identity,"【GPT社区】账号注册",String.format("您的GPT社区注册验证码为:%s，有效期10分钟",verifyCode),EmailBizTypeEnum.REGISTER.getType());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override public void register(UserRegisterRequest request){
        boolean canUsed = identityCanUsed(request.getIdentity());
        AssertUtil.isTrue(!canUsed,"该邮箱已注册");
        // 校验邮箱验证码
        //String redisKey = String.format(REGISTER_VERIFY_CODE,request.getIdentity());
        //String verifyCode = redisTemplate.opsForValue().get(redisKey);
        //AssertUtil.isEmpty(verifyCode,"验证码已过期，请重新发送");
        //AssertUtil.isTrue(!Objects.equals(verifyCode,request.getVerificationCode()),"验证码错误");

        // 验证通过，生成基础用户信息
        SysUser sysUser = userService.createUser();
        if (Tools.isNotEmpty(request.getInviter())) {
            executor.submit(() -> {
                userInviteService.inviteUser(SysUserInvite.builder().inviteUserId(sysUser.getId()).inviteUserName(request.getIdentity()).userId(request.getInviter()).build());
            });
        }

        String salt = RandomUtil.randomString(6);
        // 构建新的邮箱信息
        SysUserEmail sysUserEmail = SysUserEmail.builder()
                .password(this.encryptPassword(request.getPassword(),salt))
                .salt(salt)
                .email(request.getIdentity())
                .userId(sysUser.getId())
                .build();
        // 存储邮箱信息
        userEmailService.save(sysUserEmail);
    }

    @Override public UserLoginVO login(UserLoginRequest request){
        // 验证账号信息
        SysUserEmail userEmail = userEmailService.getByEmail(request.getUsername());
        String ip = WebUtil.getIp();
        AssertUtil.isEmpty(userEmail,"请求ip"+ip+",邮箱未注册");

        // 二次加密，验证账号密码
        String salt = userEmail.getSalt();
        String encryptPassword = encryptPassword(request.getPassword(),salt);

        Integer userId = userEmail.getUserId();
        if (!Objects.equals(encryptPassword,userEmail.getPassword())) {
            // 记录登录失败日志
            executor.submit(() -> userLoginService.loginFail(UserRegisterTypeEnum.EMAIL,request.getUsername(),userId,"账号或密码错误"));
            throw new ServiceException("账号或密码错误");
        }
        // 获取登录用户信息
        executor.submit(() -> userLoginService.loginSuccess(UserRegisterTypeEnum.EMAIL,request.getUsername(),userId));

        UserInfoVO userInfo = getUserInfo(userEmail.getId());
        if(userInfo == null){
            userInfo = UserInfoVO.builder().build();
            userInfo.setUsername(request.getUsername());
            UserInfoVO.UserVO userVo= new UserInfoVO.UserVO();
            userVo.setUserId(userId);
            userVo.setNickname(request.getUsername());
            userInfo.setUser(userVo);
        }
        // 执行登录
        StpUtil.login(userId,SaLoginConfig.setExtra(JWT_USER_INFO,userInfo));

        return UserLoginVO.builder().token(StpUtil.getTokenValue()).userId(userId).build();
    }

    @Override public UserInfoVO getUserInfo(Integer extraInfoId){
        SysUserEmail userEmail = userEmailService.getById(extraInfoId);
        // 查找基础用户信息
        if(userEmail == null){
            return  null;
        }
        SysUser sysUser = userService.getById(userEmail.getUserId());
        if (Objects.isNull(sysUser)) {
            throw new ServiceException(StrUtil.format("用户不存在：{}",extraInfoId));
        }

        // 封装基础用户信息并返回
        UserInfoVO.UserVO userVO = UserConvert.INSTANCE.entityToVo(sysUser);
        return UserInfoVO.builder().userRegisterType(UserRegisterTypeEnum.EMAIL)
                .extraId(extraInfoId).username(userEmail.getEmail())
                .user(userVO).build();
    }

    /**
     * 密码加密
     * @param password 密码
     * @param salt     盐值
     * @return java.lang.String
     * @author lcy
     * @date 2023/7/12 18:16
     **/
    private String encryptPassword(String password,String salt){
        return MD5.create().digestHex16(password + salt,StandardCharsets.UTF_8);
    }
}
