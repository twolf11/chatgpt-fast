package com.aigcfast.chat.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 是否使用枚举
 * @Author lcy
 * @Date 2023/7/12 12:08
 */
@Getter
@AllArgsConstructor
public enum CdkeyUseEnum {

    /**
     * 未使用
     */
    NO(0),

    /**
     * 已使用
     */
    YES(1);

    @EnumValue
    private final Integer isUse;
}
