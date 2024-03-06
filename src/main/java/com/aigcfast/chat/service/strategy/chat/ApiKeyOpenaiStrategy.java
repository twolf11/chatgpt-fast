package com.aigcfast.chat.service.strategy.chat;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.aigcfast.chat.service.chat.ApiInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import cn.hutool.json.JSONUtil;
import com.aigcfast.chat.common.enums.ModelEnum;
import com.aigcfast.chat.config.ConsumptionConfig;
import com.aigcfast.chat.domain.entity.chat.ChatMessage;
import com.aigcfast.chat.domain.request.chat.ChatMessageRequest;
import com.aigcfast.chat.openai.client.ChatStreamClient;
import com.aigcfast.chat.openai.listener.ParsedMessageListener;
import com.aigcfast.chat.openai.parser.ChatCompletionResponseParser;
import com.aigcfast.chat.service.sys.SysUserService;
import com.aigcfast.chat.util.AssertUtil;
import com.aigcfast.chat.util.Tools;
import com.aigcfast.chat.util.UserUtil;
import com.unfbx.chatgpt.entity.chat.ChatCompletion;
import com.unfbx.chatgpt.entity.chat.Message;
import com.unfbx.chatgpt.utils.TikTokensUtil;
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
public class ApiKeyOpenaiStrategy extends AbstractOpenaiStrategy {

    private  static  final  Logger logger = LoggerFactory.getLogger(ApiKeyOpenaiStrategy.class);

    private ChatCompletionResponseParser parser;

    private ChatStreamClient chatStreamClient;

    private ConsumptionConfig consumptionConfig;

    private SysUserService sysUserService;

    

    @Override public ApiInfo getApiKey(String userId,String modelName,ChatMessageRequest chatMessageRequest){
        ApiInfo apikey = apiKeyService.getApikey(userId,modelName,chatMessageRequest.getConversationId());
        logger.info("使用api方式:"+apikey.getKey());
        return apikey;
    }

    @Override public void checkConsumption(){
        if (consumptionConfig.isEnable()) {
            Integer memberNumber = sysUserService.getMemberNumber(UserUtil.getUserId());
            AssertUtil.isTrue(memberNumber <= 0,"消息次数不足");
        }
    }

    @Override public void sendMessage(ChatMessageRequest request,ResponseBodyEmitter emitter,ApiInfo apiInfo){
        ModelEnum model = Tools.isNotEmpty(request.getModelName()) ? ModelEnum.getByName(request.getModelName()) : ModelEnum.GPT_3_5_TURBO;
        AssertUtil.isEmpty(model,"模型不存在");
        request.setModelName(model.getName());

        List<Message> systemMessages = request.getSystemMessages();
        List<Message> messages = request.getMessages();
        if(systemMessages != null){
            messages.addAll(0,systemMessages);
        }

        //校验token长度
        int token = checkToken(model,messages);

        // 构建聊天参数
        ChatCompletion chatCompletion = ChatCompletion.builder()
                .model(model.getName())
                //预留100个token防止计算错误
                .maxTokens(model.getMaxToken() - token - 100)
                .temperature(0.8)
                // 每次生成一条
                .n(1)
                .presencePenalty(1)
                .messages(messages)
                .stream(true)
                .build();
        ChatMessage chatMessage = initChatMessage(request,apiInfo);
        chatMessage.setOriginalData(JSONUtil.toJsonStr(chatCompletion));
        // 构建事件监听器
        ParsedMessageListener parsedMessageListener = new ParsedMessageListener.Builder()
                .questionMessage(chatMessage)
                .parser(parser)
                .answerMessageId(UUID.randomUUID().toString())
                .emitter(emitter)
                .build();
        chatStreamClient.buildStreamClient(chatMessage.getApiKey()).streamChatCompletion(chatCompletion,parsedMessageListener);
    }

    /**
     * 校验token长度
     * @param model    模型
     * @param messages 消息类型
     * @return void
     * @author lcy
     * @date 2023/7/14 17:31
     **/
    protected int checkToken(ModelEnum model,List<Message> messages){
        // 当前模型最大 tokens
        int maxTokens = model.getMaxToken();
        int tokens = TikTokensUtil.tokens(model.getName(),messages);
        // 判断 token 数量是否超过限制
        AssertUtil.isEmpty(tokens > maxTokens,"当前上下文字数已经达到上限，请关闭上下文或开启新的对话");
        return tokens;
    }

}
