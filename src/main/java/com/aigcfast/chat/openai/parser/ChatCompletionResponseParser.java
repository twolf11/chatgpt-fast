package com.aigcfast.chat.openai.parser;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.unfbx.chatgpt.entity.chat.ChatChoice;
import com.unfbx.chatgpt.entity.chat.ChatCompletionResponse;
import com.unfbx.chatgpt.entity.chat.Message;

/**
 * 响应解析器
 * @Author lcy
 * @Date 2023/7/12 10:46
 */
@Component
public class ChatCompletionResponseParser implements ResponseParser<ChatCompletionResponse> {

    @Override public ChatCompletionResponse parseSuccess(String originalData){
        return JSONUtil.toBean(originalData,ChatCompletionResponse.class);
    }

    @Override public String parseReceivedMessage(String receivedMessage,String newMessage){
        return receivedMessage.concat(newMessage);
    }

    @Override public String parseNewMessage(String originalData){
        Message message = getMessage(originalData);
        if (Objects.isNull(message)) {
            return null;
        }
        return message.getContent();
    }

    @Override public String parseConversationId(String originalData){
        return null;
    }

    @Override
    public String parseAnswerMessageId(String originalData) {
        return null;
    }

    /**
     * 获取消息
     * @param originalData 原始数据
     * @return 消息
     */
    private Message getMessage(String originalData){
        ChatCompletionResponse response = parseSuccess(originalData);
        List<ChatChoice> choices = response.getChoices();
        if (CollectionUtil.isEmpty(choices)) {
            return null;
        }
        return choices.get(0).getDelta();
    }
}
