package com.aigcfast.chat.service.strategy.chat;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.aigcfast.chat.service.chat.ApiInfo;
import com.aigcfast.chat.service.chat.WebTokenSelectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import cn.hutool.json.JSONUtil;
import com.aigcfast.chat.common.enums.ConversationModelEnum;
import com.aigcfast.chat.domain.dto.chat.ConversationDto;
import com.aigcfast.chat.domain.entity.chat.ChatMessage;
import com.aigcfast.chat.domain.request.chat.ChatMessageRequest;
import com.aigcfast.chat.openai.client.WebApiClient;
import com.aigcfast.chat.openai.listener.ParsedMessageListener;
import com.aigcfast.chat.openai.parser.WebApiResponseParser;
import com.aigcfast.chat.util.AssertUtil;
import com.aigcfast.chat.util.Tools;
import com.unfbx.chatgpt.entity.chat.Message;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 聊天service
 * @Author lcy
 * @Date 2023/7/12 17:43
 */
@Slf4j
@AllArgsConstructor
@Service
public class WebApiOpenAiStrategy extends AbstractOpenaiStrategy {

    private  static  final  Logger logger = LoggerFactory.getLogger(WebApiOpenAiStrategy.class);


    private WebApiResponseParser parser;

    private WebApiClient webApiClient;

    private WebTokenSelectService webTokenSelectService;

    @Override public ApiInfo getApiKey(String userId,String modelName,ChatMessageRequest chatMessageRequest){
        ApiInfo apiInfo = webTokenSelectService.select(userId,chatMessageRequest.getConversationId());
        if(apiInfo == null){
            return null;
        }
        logger.info("使用webToken方式:"+apiInfo.getKey().hashCode());
        return apiInfo;
    }

    @Override public void checkConsumption(){

    }

    @Override public void sendMessage(ChatMessageRequest request,ResponseBodyEmitter emitter,ApiInfo apiInfo){
        ConversationModelEnum model = Tools.isNotEmpty(request.getModelName()) ? ConversationModelEnum.getByName(request.getModelName()) : ConversationModelEnum.DEFAULT_GPT_3_5;
        AssertUtil.isEmpty(model,"模型不存在");
        request.setModelName(model.getName());

        if(apiInfo.isChangeKey()){
//            String content  = "这是我们刚才对话的记录:";
//            int max = 5;
//            List<Message> messages = request.getMessages();
//            int maxI = messages.size()>=max?max:messages.size();
//            for (int i = 0; i <maxI; i++) {
//                Message message = messages.get(i);
//                if("".equals(message.getContent()))
//                    continue;
//                content+=message.getRole()+":"+message.getContent()+"。";
//            }
//            content+="。接下来请根据刚才的记录直接回答我的问题:"+request.getContent();
//            request.setContent(content);
        }

        ChatMessage chatMessage = initChatMessage(request,apiInfo);
        ConversationDto conversationDto = buildConversationRequest(chatMessage);
        chatMessage.setOriginalData(JSONUtil.toJsonStr(conversationDto));
        // 构建事件监听器
        ParsedMessageListener parsedMessageListener = new ParsedMessageListener.Builder()
                .questionMessage(chatMessage)
                .parser(parser)
                //.answerMessageId(UUID.randomUUID().toString())
                .emitter(emitter)
                .build();
        webApiClient.streamChatCompletions(chatMessage.getApiKey(),conversationDto,parsedMessageListener);
    }

    /**
     * 构建 ConversationRequest
     * @param chatMessage 聊天消息
     * @return ConversationRequest
     */
    private ConversationDto buildConversationRequest(ChatMessage chatMessage){
        // 构建 content
        ConversationDto.Content content = ConversationDto.Content.builder()
                .parts(Collections.singletonList(chatMessage.getContent()))
                .build();

        // 构建 Message
        ConversationDto.Message message = ConversationDto.Message.builder()
                .id(chatMessage.getMessageId())
                .role(Message.Role.USER.getName())
                .content(content)
                .build();

        // 构建 ConversationRequest
        return ConversationDto.builder()
                .messages(Collections.singletonList(message))
                .action(ConversationDto.MessageActionTypeEnum.NEXT)
                .model(ConversationModelEnum.DEFAULT_GPT_3_5)
                // 父级消息 id 不能为空，不然会报错，因此第一条消息也需要随机生成一个
                .parentMessageId(Tools.isEmpty(chatMessage.getParentMessageId()) ? UUID.randomUUID().toString() : chatMessage.getParentMessageId())
                .conversationId(chatMessage.getConversationId())
                .build();
    }

}
