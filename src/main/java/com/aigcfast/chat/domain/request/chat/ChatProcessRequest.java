package com.aigcfast.chat.domain.request.chat;

import java.util.List;

import com.unfbx.chatgpt.entity.chat.Message;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author lcy
 * @date 2023-3-23 消息处理请求
 */
@Data
@Schema(title = "消息处理请求")
public class ChatProcessRequest {

    @Size(min = 1, max = 7500, message = "问题字数范围[1, 7500]")
    @Schema(title = "问题")
    @NotBlank(message = "问题不能为空")
    private String prompt;

    @Schema(title = "配置")
    private Options options;

    @Schema(title = "上下文内容，提问的问题也需要封装进去")
    @NotEmpty(message = "消息不能为空")
    private List<Message> messages;

    @Schema(title = "系统消息")
    private String systemMessage;


    @Schema(title = "客户端版本号")
    private Long version;

    @Data
    @Schema(title = "消息配置")
    public static class Options {

        @Schema(title = "聊天室id")
        private Long roomId;

        @Schema(title = "对话 id")
        private String conversationId;

        /**
         * 这里的父级消息指的是回答的父级消息 id 前端发送问题，需要上下文的话传回答的父级消息 id
         */
        @Schema(title = "父级消息 id")
        private String parentMessageId;

        @Schema(title = "是否快速相应模式")
        private Boolean fast;
    }

    public ChatMessageRequest toNewRequest(){
        ChatMessageRequest chatMessageRequest = new ChatMessageRequest();
        chatMessageRequest.setContent(prompt);
        chatMessageRequest.setRoomId(options.roomId);
        List<Message> lastThreeMessages = messages;
        chatMessageRequest.setMessages(lastThreeMessages);
        chatMessageRequest.setParentMessageId(options.parentMessageId);
        chatMessageRequest.setConversationId(options.getConversationId());
        chatMessageRequest.setFast(options.getFast());
        chatMessageRequest.setVersion(version);
        return chatMessageRequest;
    }
}
