package com.aigcfast.chat.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * TODO
 * @Author lcy
 * @Date 2023/5/22 20:32
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "register")
public class RegisterConfig {

    /** 默认数 */
    private int initNumber;

    /** 邀请默认数 */
    private int inviteNumber;

    /** 注册地址 */
    private String registerAddr;

}
