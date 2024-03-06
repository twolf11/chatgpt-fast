package com.aigcfast.chat.domain.request.chat;

import java.util.List;

import com.aigcfast.chat.common.enums.ApiTypeEnum;
import com.unfbx.chatgpt.entity.chat.Message;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 聊天参数
 * @Author lcy
 * @Date 2022/5/6 16:20
 */
@Data
@Schema(title = "消息处理请求")
public class ChatMessageRequest {

    @Schema(title = "模型名称")
    private String modelName;

    @Schema(title = "api类型")
    private ApiTypeEnum apiType = ApiTypeEnum.API_KEY;

    @Schema(title = "聊天室id")
    private Long roomId;

    @Schema(title = "快速响应模式")
    private Boolean fast = false;

    /**
     * 上次会话的消息id，一般是回复的消息，如果是异常的信息则传上次问题id
     */
    @Schema(title = "父级消息id")
    private String parentMessageId;

    /** token会话时用到 */
    private String conversationId;

    @Size(min = 1, max = 5000, message = "单次提问内容不超过5000")
    @Schema(title = "问题内容")
    @NotBlank(message = "问题内容不能为空")
    private String content;

    @Schema(title = "上下文内容，提问的问题也需要封装进去")
    @NotEmpty(message = "消息不能为空")
    private List<Message> messages;

    @Schema(title = "前置上下文")
    private List<Message> systemMessages;

    @Schema(title = "客户端版本号")
    private Long version;
}
