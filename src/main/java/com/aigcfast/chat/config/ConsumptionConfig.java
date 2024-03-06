package com.aigcfast.chat.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * 消费配置
 * @Author lcy
 * @Date 2023/5/31 15:08
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "consumption")
public class ConsumptionConfig {

    /** 是否开启消费次数 */
    private boolean enable = false;

}
