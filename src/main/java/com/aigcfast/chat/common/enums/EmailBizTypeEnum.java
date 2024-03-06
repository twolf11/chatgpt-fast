package com.aigcfast.chat.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 邮箱业务类型枚举
 * @Author lcy
 * @Date 2023/7/12 12:08
 */
@Getter
@AllArgsConstructor
public enum EmailBizTypeEnum {

    /**
     * 用户注册
     */
    REGISTER("REGISTER","注册认证"),

    /**
     * 用户找回密码
     */
    RETRIEVE_PASSWORD("RETRIEVE_PASSWORD","找回密码认证");

    @EnumValue
    private final String type;

    private final String desc;
}
