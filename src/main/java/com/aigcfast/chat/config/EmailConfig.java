package com.aigcfast.chat.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.mail.MailAccount;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 邮箱参数配置
 * @Author lcy
 * @Date 2022/5/6 16:20
 */
@Slf4j
@Data
@Configuration
@ConfigurationProperties(prefix = "email")
public class EmailConfig {

    /**
     * 邮箱服务器地址
     */
    private String host;

    /**
     * 邮箱服务器端口，默认25
     */
    private Integer port;

    /**
     * 发件邮箱地址
     */
    private String from;

    /**
     * 发件人名称
     */
    private String user;

    /**
     * 授权码
     */
    private String pass;

    /**
     * 是否需要授权；默认需要授权
     */
    private boolean auth = true;

    /**
     * 用于判断邮箱服务是否可用，如果不可用，和邮箱相关的一些功能需要报错
     * @return true有效；false无效
     */
    public boolean isAvailable(){
        return StrUtil.isNotBlank(host) && ObjectUtil.isNotNull(port) && StrUtil.isNotBlank(from)
                && StrUtil.isNotBlank(user);
    }

    @Bean
    public MailAccount mailAccount(){
        MailAccount mailAccount = new MailAccount();
        mailAccount.setHost(host);
        mailAccount.setPort(port);
        mailAccount.setFrom(from);
        mailAccount.setUser(user);
        mailAccount.setAuth(auth);
        mailAccount.setDebug(true);
        mailAccount.setSslEnable(true);
        mailAccount.setPass(pass);
        log.info("初始化邮箱账号完毕");
        return mailAccount;
    }
}
