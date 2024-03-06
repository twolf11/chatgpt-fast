package com.aigcfast.chat.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 拓展来源枚举
 * @Author lcy
 * @Date 2023/7/12 12:08
 */
@Getter
@AllArgsConstructor
public enum ExtendSourceEnum {

    /**
     * 邀请
     */
    INVITE("INVITE"),

    /**
     * 购买
     */
    BUY("BUY");

    @EnumValue
    private final String type;
}
