package com.aigcfast.chat.openai.parser;

import java.util.Optional;

import org.springframework.stereotype.Component;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.aigcfast.chat.domain.vo.chat.ConversationResponse;
import com.aigcfast.chat.util.JsonUtil;
import com.aigcfast.chat.util.Tools;
import com.unfbx.chatgpt.entity.chat.Message;
import lombok.extern.slf4j.Slf4j;

/**
 * accessToken响应解析器
 * @Author lcy
 * @Date 2023/7/12 10:46
 */
@Slf4j
@Component
public class WebApiResponseParser implements ResponseParser<ConversationResponse> {

    @Override
    public ConversationResponse parseSuccess(String originalData){
        // 不为 JSON 直接返回 null，不知道什么情况触发，但是不属于正文
        if (!JSONUtil.isTypeJSON(originalData)) {
            return null;
        }
        return JsonUtil.jsonToObject(originalData,ConversationResponse.class);
    }

    @Override
    public String parseReceivedMessage(String receivedMessage,String newMessage){
        return newMessage;
    }

    @Override
    public String parseNewMessage(String originalData){
        return Optional.ofNullable(parseSuccess(originalData)).map(ConversationResponse :: getMessage)
                .filter(message1 -> {
                    ConversationResponse.Author author = Optional.ofNullable(message1.getAuthor())
                            .filter(author1 -> author1.getRole().equals(Message.Role.ASSISTANT.getName()))
                            .orElse(null);
                    return Tools.isNotEmpty(author);
                }).map(ConversationResponse.Message :: getContent).map(ConversationResponse.Content :: getParts)
                .map(parts -> {
                    if (CollectionUtil.isEmpty(parts)) {
                        return null;
                    } else {
                        // AccessToken 模式返回的消息每句都会包含前面的话，不需要手动拼接
                        return parts.get(0);
                    }
                }).orElse(null);
    }

    @Override public String parseConversationId(String originalData){
        return Optional.ofNullable(parseSuccess(originalData)).map(ConversationResponse :: getConversationId).orElse(null);
    }

    @Override
    public String parseAnswerMessageId(String originalData) {
        return Optional.ofNullable(parseSuccess(originalData)).map(ConversationResponse::getMessage).map(ConversationResponse.Message::getId).orElse(null);
    }
}
