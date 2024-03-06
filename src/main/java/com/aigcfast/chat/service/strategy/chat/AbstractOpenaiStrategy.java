package com.aigcfast.chat.service.strategy.chat;

import java.util.List;
import java.util.UUID;

import com.aigcfast.chat.service.chat.ApiInfo;
import com.unfbx.chatgpt.entity.chat.Message;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import com.aigcfast.chat.common.enums.ChatMessageTypeEnum;
import com.aigcfast.chat.domain.entity.chat.ChatMessage;
import com.aigcfast.chat.domain.request.chat.ChatMessageRequest;
import com.aigcfast.chat.service.chat.ApikeyService;
import com.aigcfast.chat.util.AssertUtil;
import com.aigcfast.chat.util.UserUtil;
import com.aigcfast.chat.util.WebUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * 聊天抽象策略
 * @Author lcy
 * @Date 2023/7/12 17:43
 */
@Slf4j
public abstract class AbstractOpenaiStrategy {

    @Resource
    protected ApikeyService apiKeyService;

    /**
     * 获取apiKey
     * @param modelName 模型类型
     * @return java.lang.String
     * @author lcy
     * @date 2023/8/11 11:40
     **/
    public abstract ApiInfo getApiKey(String userId,String modelName,ChatMessageRequest chatMessageRequest);

    /**
     * 校验消费次数
     * @author lcy
     * @date 2023/8/21 10:24
     **/
    public abstract void checkConsumption();

    /**
     * 发送消息
     * @param request 参数
     * @param emitter 响应流对象
     * @author lcy
     * @date 2023/7/12 17:53
     **/
    public abstract void sendMessage(ChatMessageRequest request,ResponseBodyEmitter emitter,ApiInfo apiInfo);

    /**
     * 初始化消息对象
     * @param request 请求参数
     * @return com.aigcfast.chat.domain.entity.chat.ChatMessage
     * @author lcy
     * @date 2023/7/14 19:51
     **/
    protected ChatMessage initChatMessage(ChatMessageRequest request,ApiInfo apiKey){
        AssertUtil.isEmpty(apiKey,"服务暂不可用，请联系管理员");
        ChatMessage chatMessage = ChatMessage.builder()
                .messageId(UUID.randomUUID().toString())
                .ip(WebUtil.getIp())
                .messageType(ChatMessageTypeEnum.QUESTION)
                .content(request.getContent())
                .modelName(apiKey.getModelName())
                .apiType(apiKey.getApiTypeEnum())
                .apiKey(apiKey.getKey())
                .parentMessageId(request.getParentMessageId())
                .conversationId(request.getConversationId())
                .createBy(UserUtil.getUserId())
                .build();
        if (request.getRoomId() == null) {
            chatMessage.setChatRoomId(IdWorker.getId());
        }else{
            chatMessage.setChatRoomId(request.getRoomId());

        }
        return chatMessage;
    }

}
