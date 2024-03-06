package com.aigcfast.chat.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * openAIError响应码
 * @Author lcy
 * @Date 2023/8/7 19:40
 */
@Getter
@AllArgsConstructor
public enum OpenAiErrorCodeEnum {

    ACCOUNT_DEACTIVATED("account_deactivated","账号被封了"),

    INVALID_API_KEY("invalid_api_key","key不正确"),

    BILLING_NOT_ACTIVE("billing_not_active","计费未激活"),

    INSUFFICIENT_QUOTA("insufficient_quota","额度用光了");

    /** code编码 */
    private final String code;

    /** 描述 */
    private final String desc;

    /**
     * 根据code获取枚举
     * @param code 名称
     * @return com.aigcfast.chat.common.enums.ModelEnum
     * @author lcy
     * @date 2023/7/13 20:18
     **/
    public static OpenAiErrorCodeEnum getByCode(String code){
        OpenAiErrorCodeEnum[] values = values();
        for (int i = 0; i < values.length; i++) {
            if (values[i].code.equals(code)) {
                return values[i];
            }
        }
        return null;
    }

}
