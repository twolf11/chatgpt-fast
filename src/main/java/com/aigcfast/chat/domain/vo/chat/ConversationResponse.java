package com.aigcfast.chat.domain.vo.chat;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * AccessToken 对话响应参数
 * @Author lcy
 * @Date 2023/7/12 10:46
 */
@Data
public class ConversationResponse {

    private Message message;

    /**
     * 对话 id
     */
    @JsonProperty("conversation_id")
    private String conversationId;

    /**
     * 错误信息，不清楚啥类型
     */
    private Object error;

    @Data
    public static class Message {

        private String id;

        private Author author;

        @JsonProperty("create_time")
        private BigDecimal createTime;

        @JsonProperty("update_time")
        private BigDecimal updateTime;

        private Content content;

        @JsonProperty("end_turn")
        private Boolean endTurn;

        private Double weight;

        private Map<String,Object> metadata;

        private String recipient;
    }

    @Data
    public static class Author {

        private String role;

        private String name;

        private Map<String,Object> metadata;
    }

    @Data
    public static class Content {

        @JsonProperty("content_type")
        private String contentType;

        private List<String> parts;
    }
}
