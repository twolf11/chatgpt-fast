package com.aigcfast.chat.domain.entity.chat;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.aigcfast.chat.common.enums.ApiTypeEnum;
import com.aigcfast.chat.common.enums.ChatMessageTypeEnum;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 聊天消息表
 * @Author lcy
 * @Date 2023-07-12
 */
@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName("chat_message")
public class ChatMessage implements Serializable {

    /** 主键 */
    private Long id;

    /** 消息 id */
    private String messageId;

    /** 父级消息 id */
    private String parentMessageId;

    /** 消息类型 */
    private ChatMessageTypeEnum messageType;

    /** 聊天室 id */
    private Long chatRoomId;

    /** 模型名称 */
    private String modelName;

    /** ApiKey */
    private String apiKey;

    /** api类型 */
    private ApiTypeEnum apiType;

    /** 消息内容 */
    private String content;

    /** 消息的原始请求或响应数据 */
    private String originalData;

    /** 错误的响应数据 */
    private String responseErrorData;

    /** ip */
    private String ip;

    /** 用户 id */
    private Integer createBy;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间 */
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private String conversationId;



}
