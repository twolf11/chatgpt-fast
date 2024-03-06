package com.aigcfast.chat.service.log;

import com.aigcfast.chat.domain.entity.log.LogUserLogin;
import com.aigcfast.chat.common.enums.UserRegisterTypeEnum;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 前端用户登录日志表service
 * @Author lcy
 * @Date 2023-07-12
 */
public interface LogUserLoginService extends IService<LogUserLogin> {

    /**
     * 登录成功日志
     * @param registerType 注册类型
     * @param username     用户名
     * @param userId       userId
     * @author lcy
     * @date 2023/7/12 18:38
     **/
    void loginSuccess(UserRegisterTypeEnum registerType,String username,Integer userId);

    /**
     * 登录失败日志
     * @param registerType 注册类型
     * @param username     用户名
     * @param userId       userId
     * @param message      消息
     * @author lcy
     * @date 2023/7/12 18:38
     **/
    void loginFail(UserRegisterTypeEnum registerType,String username,Integer userId,String message);

}
