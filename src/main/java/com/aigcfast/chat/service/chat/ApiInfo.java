package com.aigcfast.chat.service.chat;

import com.aigcfast.chat.common.enums.ApiTypeEnum;
import com.aigcfast.chat.service.strategy.chat.AbstractOpenaiStrategy;
import lombok.Data;

@Data
public class ApiInfo {

    private AbstractOpenaiStrategy abstractOpenaiStrategy;

    String modelName;

    String key;


    ApiTypeEnum apiTypeEnum;


    boolean changeKey = false;

}
