package com.aigcfast.chat.domain.vo.chat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 聊天回复的消息
 * @Author lcy
 * @Date 2023/7/12 10:46
 */
@Data
@Schema(title = "聊天回复的消息")
public class ChatReplyMessageVO {

    @Schema(title = "角色")
    private String role;

    @Schema(title = "当前消息id")
    private String messageId;

    @Schema(title = "父级消息id")
    private String parentMessageId;

    @Schema(title = "聊天室 id")
    private Long roomId;

    @Schema(title = "回复的内容")
    private String text;

    @Schema(title = "对话 id")
    private String conversationId;
}
