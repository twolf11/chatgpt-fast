package com.aigcfast.chat.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * AccessToken 对话模型
 * @Author lcy
 * @Date 2023/7/12 12:08
 */
@AllArgsConstructor
public enum ConversationModelEnum implements IModelType {

    /**
     * 对应官网 Default 3.5 模型
     */
    DEFAULT_GPT_3_5("gpt-3.5-token","text-davinci-002-render-sha"),

    /**
     * 对应官网 Legacy 3.5 模型 ChatGPT Plus
     */
    LEGACY_GPT_3_5("gpt-3.5-token","text-davinci-002-render-paid"),

    /**
     * 对应官网 GPT-4 模型 目前限制 3 小时 25 条消息 ChatGPT Plus
     */
    GPT_4("gpt-4-token","gpt-4");

    @Getter
    private final String type;

    @Getter
    @JsonValue
    private final String name;

    /**
     * 根据名称获取模型
     * @param name 名称
     * @return com.aigcfast.chat.common.enums.ModelEnum
     * @author lcy
     * @date 2023/7/13 20:18
     **/
    public static ConversationModelEnum getByName(String name){
        ConversationModelEnum[] values = values();
        for (int i = 0; i < values.length; i++) {
            if (values[i].name.equals(name)) {
                return values[i];
            }
        }
        return null;
    }
}
