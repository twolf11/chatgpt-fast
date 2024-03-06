package com.aigcfast.chat.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 模型枚举
 * @Author lcy
 * @Date 2023/6/14 16:57
 */
@Getter
@AllArgsConstructor
public enum ModelEnum implements IModelType {
    /**
     * GPT3.5 Token 限制文档
     * @see <a href="https://platform.openai.com/docs/models/gpt-3-5">GPT3.5 Token 限制文档</a>
     */
    GPT_3_5_TURBO("gpt-3.5","gpt-3.5-turbo",4096,1),
    GPT_3_5_TURBO_0613("gpt-3.5","gpt-3.5-turbo-0613",4096,1),
    GPT_3_5_TURBO_0301("gpt-3.5","gpt-3.5-turbo-0301",4096,1),
    GPT_3_5_TURBO_16K("gpt-3.5","gpt-3.5-turbo-16k",16384,1),
    GPT_3_5_TURBO_16K_0613("gpt-3.5","gpt-3.5-turbo-0613",16384,1),
    WebApi("WebApi","WebApi",16384,1),
    /**
     * GPT4 Token 限制文档
     * @see <a href="https://platform.openai.com/docs/models/gpt-4">GPT4Token 限制文档</a>
     */
    GPT_4("gpt-4","gpt-4",8192,1),
    GPT_4_0314("gpt-4","gpt-4-0314",8192,1),
    /**
     * 支持函数
     */
    GPT_4_0613("gpt-4","gpt-4-0613",8192,1),
    /**
     * GPT4.0 超长上下文
     */
    GPT_4_32K("gpt-4","gpt-4-32k",32768,1),
    /**
     * GPT4.0 超长上下文
     */
    GPT_4_32K_0613("gpt-4","gpt-4-32k-0613",32768,1);

    /** 模型类别 */
    private final String type;

    /** 模型名称 */
    private final String name;

    /** 模型token长度 */
    private final int maxToken;

    /** 使用次数 */
    private final int number;

    /**
     * 根据名称获取模型
     * @param name 名称
     * @return com.aigcfast.chat.common.enums.ModelEnum
     * @author lcy
     * @date 2023/7/13 20:18
     **/
    public static ModelEnum getByName(String name){
        ModelEnum[] values = values();
        for (int i = 0; i < values.length; i++) {
            if (values[i].name.equals(name)) {
                return values[i];
            }
        }
        return null;
    }
}
