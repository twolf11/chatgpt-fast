
package com.aigcfast.chat.service.log.impl;

import org.springframework.stereotype.Service;

import com.aigcfast.chat.domain.entity.log.LogUserLogin;
import com.aigcfast.chat.common.enums.UserRegisterTypeEnum;
import com.aigcfast.chat.mapper.log.LogUserLoginMapper;
import com.aigcfast.chat.service.log.LogUserLoginService;
import com.aigcfast.chat.service.sys.SysUserService;
import com.aigcfast.chat.util.WebUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 前端用户登录日志表service实现
 * @Author lcy
 * @Date 2023-07-12
 */
@Service
@AllArgsConstructor
@Slf4j
public class LogUserLoginServiceImpl extends ServiceImpl<LogUserLoginMapper,LogUserLogin> implements LogUserLoginService {

    private SysUserService sysUserService;

    @Override public void loginSuccess(UserRegisterTypeEnum registerType,String username,Integer userId){
        LogUserLogin userLogin = LogUserLogin.builder().userId(userId).loginType(UserRegisterTypeEnum.EMAIL).loginUsername(username)
                .loginIp(WebUtil.getIp()).loginStatus(true).message("success").build();
        save(userLogin);
        sysUserService.updateLastLoginIp(userId,userLogin.getLoginIp());
    }

    @Override public void loginFail(UserRegisterTypeEnum registerType,String username,Integer userId,String message){
        LogUserLogin userLogin = LogUserLogin.builder().userId(userId).loginType(UserRegisterTypeEnum.EMAIL).loginUsername(username)
                .loginIp(WebUtil.getIp()).loginStatus(false).message(message).build();
        save(userLogin);
    }
}
