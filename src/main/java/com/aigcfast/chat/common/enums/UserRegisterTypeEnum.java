package com.aigcfast.chat.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户注册枚举
 * @Author lcy
 * @Date 2023/7/12 10:53
 */
@Getter
@AllArgsConstructor
public enum UserRegisterTypeEnum {

    /**
     * 邮箱注册
     */
    EMAIL("EMAIL","邮箱","userEmailStrategy"),

    /**
     * 手机号注册
     */
    PHONE("PHONE","手机号","");

    @EnumValue
    private final String code;

    private final String desc;

    /** bean的名称 */
    private final String beanName;

}
