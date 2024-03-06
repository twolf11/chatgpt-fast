package com.aigcfast.chat.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * api类型枚举
 * @Author lcy
 * @Date 2023/6/14 16:57
 */
@AllArgsConstructor
public enum ApiTypeEnum {

    /**
     * API_KEY
     */
    API_KEY("ApiKey","gpt-3.5-turbo","ChatGPTAPI","apiKeyOpenaiStrategy"),

    /**
     * WEB_API--ACCESS_TOKEN
     */
    WEB_API("WebApi","text-davinci-002-render-sha","ChatGPTUnofficialProxyAPI","webApiOpenAiStrategy");

    @Getter
    @EnumValue
    private final String name;

    @Getter
    @EnumValue
    private final String defautModelName;

    @Getter
    @JsonValue
    private final String message;

    @Getter
    @JsonValue
    private final String beanName;
}
