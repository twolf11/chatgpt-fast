package com.aigcfast.chat.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 聊天消息类型枚举
 * @Author lcy
 * @Date 2023/7/12 12:08
 */
@Getter
@AllArgsConstructor
public enum ChatMessageTypeEnum {

    /**
     * 问题
     */
    QUESTION("QUESTION"),

    /**
     * 回答
     */
    ANSWER("ANSWER");

    @EnumValue
    private final String type;
}
