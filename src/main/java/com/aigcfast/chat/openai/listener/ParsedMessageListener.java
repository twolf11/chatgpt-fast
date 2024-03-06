package com.aigcfast.chat.openai.listener;

import java.io.IOException;
import java.util.Objects;

import com.aigcfast.chat.service.chat.WebTokenSelectService;
import com.aigcfast.chat.service.chat.impl.WebTokenSelectServiceImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.aigcfast.chat.common.enums.ApiTypeEnum;
import com.aigcfast.chat.common.enums.ModelEnum;
import com.aigcfast.chat.common.enums.OpenAiErrorCodeEnum;
import com.aigcfast.chat.common.exception.ServiceException;
import com.aigcfast.chat.domain.entity.chat.ChatMessage;
import com.aigcfast.chat.domain.vo.chat.ChatReplyMessageVO;
import com.aigcfast.chat.openai.parser.ResponseParser;
import com.aigcfast.chat.service.chat.ApikeyService;
import com.aigcfast.chat.service.chat.ChatMessageService;
import com.aigcfast.chat.util.JsonUtil;
import com.aigcfast.chat.util.Tools;
import com.unfbx.chatgpt.entity.chat.Message;
import com.unfbx.chatgpt.entity.common.OpenAiResponse;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;

/**
 * 解析返回数据监听器
 *
 * @Author lcy
 * @Date 2023/7/12 10:46
 */
@Slf4j
public class ParsedMessageListener extends EventSourceListener {

    /**
     * 解析器
     */
    private final ResponseParser<?> parser;

    /**
     * 问题消息
     */
    private final ChatMessage questionMessage;

    /**
     * 响应emitter
     */
    private final ResponseBodyEmitter emitter;

    /**
     * 回复消息id
     */
    private String answerMessageId;

    /**
     * 收到的解析完的数据
     */
    private String receivedMessage;

    /**
     * 收到的原始数据
     */
    private String receivedOriginalData;

    /**
     * 当前消息流条数
     */
    private int currentStreamMessageCount;

    public ParsedMessageListener(Builder builder) {
        this.parser = builder.parser;
        this.questionMessage = builder.questionMessage;
        this.emitter = builder.emitter;
        //this.answerMessageId = builder.answerMessageId;
        this.receivedMessage = "";
        this.receivedOriginalData = "";
        this.currentStreamMessageCount = 0;
    }

    @Override
    public void onOpen(@NotNull EventSource eventSource, @NotNull Response response) {

    }

    @Override
    public void onEvent(@NotNull EventSource eventSource, String id, String type, @NotNull String originalData) {
        // 判断有没有结束
        boolean isEnd = Objects.equals(originalData, "[DONE]");
        String newMessage;
        ChatReplyMessageVO chatReplyMessageVO;
        if (isEnd) {
            chatReplyMessageVO = new ChatReplyMessageVO();
            chatReplyMessageVO.setText("");
            chatReplyMessageVO.setMessageId(questionMessage.getMessageId());
            chatReplyMessageVO.setRole(Message.Role.ASSISTANT.getName());
            chatReplyMessageVO.setParentMessageId(answerMessageId);
            chatReplyMessageVO.setRoomId(questionMessage.getChatRoomId());
            chatReplyMessageVO.setConversationId(questionMessage.getConversationId());
            String str = JsonUtil.objectToJson(chatReplyMessageVO);
            try {
                emitter.send((currentStreamMessageCount != 1 ? "\n" : "") + str);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            emitter.complete();
            ChatMessageService chatMessageService = SpringUtil.getBean(ChatMessageService.class);
            WebTokenSelectServiceImpl webTokenSelectService = SpringUtil.getBean(WebTokenSelectServiceImpl.class);
            webTokenSelectService.releaseUser(questionMessage.getCreateBy().toString());
            try {
                chatMessageService.saveMessage(questionMessage, answerMessageId, receivedMessage, receivedOriginalData);
            } catch (Exception e) {
                log.error("保存聊天数据失败！");
            }
        } else {
            if (ApiTypeEnum.WEB_API.equals(questionMessage.getApiType()) && Tools.isEmpty(questionMessage.getConversationId())) {
                String conversationId = parser.parseConversationId(originalData);
                questionMessage.setConversationId(conversationId);
            }
            // 解析消息
            newMessage = parser.parseNewMessage(originalData);
            answerMessageId = parser.parseAnswerMessageId(originalData);
            // 为空直接跳过
            if (StrUtil.isEmpty(newMessage)) {
                return;
            }
            currentStreamMessageCount++;
            // 拼接消息
            String replace = newMessage.replace(receivedMessage, "");
            receivedMessage = parser.parseReceivedMessage(receivedMessage, newMessage);
            // 记录上次响应数据拼接
            receivedOriginalData = receivedMessage.concat(originalData);
            // 构建 ChatReplyMessageVO
            chatReplyMessageVO = new ChatReplyMessageVO();
            chatReplyMessageVO.setText(replace);
            try {
                String str = JsonUtil.objectToJson(chatReplyMessageVO);
                emitter.send((currentStreamMessageCount != 1 ? "\n" : "") + str);
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        }

    }

    @Override
    public void onFailure(@NotNull EventSource eventSource, @Nullable Throwable t, @Nullable Response response) {
        String responseStr = null;
        if (t instanceof IllegalStateException) {
            log.warn("中断聊天！");
            return;
        }
        try {
            if (Objects.nonNull(response) && Objects.nonNull(response.body())) {
                responseStr = response.body().string();
            }
            String responseMessage = response.message();
            ChatReplyMessageVO chatReplyMessageVO = new ChatReplyMessageVO();
            chatReplyMessageVO.setParentMessageId(questionMessage.getParentMessageId());
            chatReplyMessageVO.setConversationId(questionMessage.getConversationId());
            chatReplyMessageVO.setRoomId(questionMessage.getChatRoomId());
            String token = this.questionMessage.getApiKey();
            log.warn("消息发送异常，当前已接收消息：{}，响应内容：{},body:{} ,  code:{}, 异常堆栈：{},token {}", receivedMessage, response.message(), responseStr, response.code(), t,token);

            if (response.code() == 429) {
                if (responseMessage.contains("Only one message") || responseMessage.contains("Too Many Requests")) {
                    //进入CD期
                    WebTokenSelectServiceImpl webTokenSelectService = SpringUtil.getBean(WebTokenSelectServiceImpl.class);
                    webTokenSelectService.entersCd(token);
                }
                throw new ServiceException("频繁请求，请稍后再试!");
            } else if (response.code() == 404) {
                throw new ServiceException("请打开新聊天！！！");
            } else if ("token_expired".equals(response.code())) {
                //进入CD期，过期
                WebTokenSelectServiceImpl webTokenSelectService = SpringUtil.getBean(WebTokenSelectServiceImpl.class);
                webTokenSelectService.entersCd(token);
                webTokenSelectService.releaseUser(questionMessage.getCreateBy().toString());
                throw new ServiceException("调用超时请重试！！！code:600");
            } else if ("account_deactivated".equals(response.code())) {
                //进入CD期，过期
                WebTokenSelectServiceImpl webTokenSelectService = SpringUtil.getBean(WebTokenSelectServiceImpl.class);
                webTokenSelectService.entersCd(token);
                throw new ServiceException("过期请求,请重试！！！code=601");
            } else if (response.code() == 504 || response.code() == 403) {
                WebTokenSelectServiceImpl webTokenSelectService = SpringUtil.getBean(WebTokenSelectServiceImpl.class);
                webTokenSelectService.entersCd(token);
                throw new ServiceException("调用超时请重试！！！code:602");
            } else if (response.code() == 401) {
                WebTokenSelectServiceImpl webTokenSelectService = SpringUtil.getBean(WebTokenSelectServiceImpl.class);
                webTokenSelectService.entersCd(token);
                throw new ServiceException("调用超时请重试code:603！！！");
            } else {
                if (ModelEnum.getByName(questionMessage.getModelName()) != null) {
                    if ("".equals(responseStr)) {
                        emitter.complete();
                    } else {
                        try {
                            OpenAiResponse openAiResponse = JSONUtil.toBean(responseStr, OpenAiResponse.class);
                            String errorCode = openAiResponse.getError().getCode();
                            OpenAiErrorCodeEnum openAiErrorCodeEnum = OpenAiErrorCodeEnum.getByCode(errorCode);
                            //账号被封或者key不正确就移除掉
                            if (openAiErrorCodeEnum != null) {
                                ApikeyService apikeyService = SpringUtil.getBean(ApikeyService.class);
                                if (apikeyService != null) {
                                    apikeyService.removeApikey(ModelEnum.getByName(questionMessage.getModelName()).getType(), questionMessage.getApiKey());
                                }
                            }
                        } catch (Exception e) {
                            log.warn("解析相应消息出错!无法进行apikey判断", e);
                        }
                    }
                }
                throw new ServiceException("消息处理异常，请勿频繁发送");
            }
        } catch (Exception e) {
            emitter.completeWithError(e);
            log.warn(response.message() + " code:" + response.code());
            log.warn("消息发送异常，处理异常发送消息时出错", e.getMessage());
        }
    }

    public static class Builder {

        /**
         * 解析器
         */
        private ResponseParser<?> parser;

        /**
         * 问题消息
         */
        private ChatMessage questionMessage;

        /**
         * 响应emitter
         */
        private ResponseBodyEmitter emitter;

        /**
         * 回复消息id
         */
        private String answerMessageId;

        public Builder parser(ResponseParser<?> parser) {
            this.parser = parser;
            return this;
        }

        public Builder questionMessage(ChatMessage questionMessage) {
            this.questionMessage = questionMessage;
            return this;
        }

        public Builder emitter(ResponseBodyEmitter emitter) {
            this.emitter = emitter;
            return this;
        }

        public Builder answerMessageId(String answerMessageId) {
            this.answerMessageId = answerMessageId;
            return this;
        }

        public ParsedMessageListener build() {

            if (parser == null) {
                throw new IllegalStateException("Parser must be set");
            }
            if (questionMessage == null) {
                throw new IllegalStateException("questionMessage must be set");
            }
            if (emitter == null) {
                throw new IllegalStateException("emitter must be set");
            }
//            if (answerMessageId == null) {
//                throw new IllegalStateException("answerMessageId must be set");
//            }
            return new ParsedMessageListener(this);
        }
    }
}
