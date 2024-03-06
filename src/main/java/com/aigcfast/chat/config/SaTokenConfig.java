package com.aigcfast.chat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.jwt.StpLogicJwtForStateless;
import cn.dev33.satoken.stp.StpLogic;
import cn.dev33.satoken.stp.StpUtil;

/**
 * sa-token配置类
 * @Author lcy
 * @Date 2023/6/25 16:47
 */
@Configuration
public class SaTokenConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        // 注册 Sa-Token 拦截器，校验规则为 StpUtil.checkLogin() 登录校验。
        registry.addInterceptor(new SaInterceptor(handle -> StpUtil.checkLogin()))
                // 放行用户端接口
                .excludePathPatterns("/user/register/sendEmail","/user/register/email","/user/login/email","/cache/reloadToken","/cache/reload","/cache/reloadApikey");
    }

    @Bean
    public StpLogic getStpLogicJwt(){
        return new StpLogicJwtForStateless();
    }
}
