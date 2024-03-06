package com.aigcfast.chat.service.strategy.user;

import com.aigcfast.chat.domain.request.sys.UserLoginRequest;
import com.aigcfast.chat.domain.request.sys.UserRegisterRequest;
import com.aigcfast.chat.domain.vo.sys.UserInfoVO;
import com.aigcfast.chat.domain.vo.sys.UserLoginVO;

/**
 * 抽象用户类型策略
 * @Author lcy
 * @Date 2023/7/12 17:43
 */
public abstract class AbstractUserTypeStrategy {

    /**
     * 验证用户名是否能使用
     * @param identity 用户名
     * @return true有效，false无效
     */
    public abstract boolean identityCanUsed(String identity);

    /**
     * 发送验证码
     * @param identity 用户账号
     * @author lcy
     * @date 2023/7/12 17:56
     **/
    public abstract void sendRegisterVerifyCode(String identity);

    /**
     * 用户注册
     * @param request 注册参数
     * @author lcy
     * @date 2023/7/12 17:53
     **/
    public abstract void register(UserRegisterRequest request);

    /**
     * 用户登录
     * @param request 登录参数
     * @return com.aigcfast.chat.web.domain.vo.sys.UserLoginVO
     * @author lcy
     * @date 2023/7/12 17:55
     **/
    public abstract UserLoginVO login(UserLoginRequest request);

    /**
     * 获取登录用户信息
     * @param extraInfoId 绑定信息表ID
     * @return 登录用户信息
     */
    public abstract UserInfoVO getUserInfo(Integer extraInfoId);

}
