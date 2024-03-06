package com.aigcfast.chat.service.strategy.chain;

import java.util.Optional;

import com.aigcfast.chat.common.enums.ApiTypeEnum;
import com.aigcfast.chat.service.chat.ApiInfo;
import com.aigcfast.chat.service.strategy.chat.ApiInfoFactory;
import com.aigcfast.chat.util.UserUtil;
import com.unfbx.chatgpt.entity.chat.Message;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import cn.hutool.extra.spring.SpringUtil;
import com.aigcfast.chat.domain.request.chat.ChatMessageRequest;
import com.aigcfast.chat.service.strategy.chat.AbstractOpenaiStrategy;
import lombok.extern.slf4j.Slf4j;

/**
 * api聊天策略
 * @Author lcy
 * @Date 2023/7/12 17:43
 */
@Slf4j
public class OpenAiChatEmitterChain extends AbstractResponseEmitterChain {

    @Override public void doChain(ChatMessageRequest request,ResponseBodyEmitter emitter){
        request.setContent(request.getContent());
        Integer userId = UserUtil.getUserId();
        ApiInfo apiKey =  ApiInfoFactory.get(request);
        if (apiKey.isChangeKey()) {
            request.setConversationId(null);
            request.setParentMessageId(null);
        }
        AbstractOpenaiStrategy abstractOpenaiStrategy = apiKey.getAbstractOpenaiStrategy();
        abstractOpenaiStrategy = SpringUtil.getBean(apiKey.getApiTypeEnum().getBeanName());
        abstractOpenaiStrategy.checkConsumption();
        abstractOpenaiStrategy.sendMessage(request,emitter,apiKey);
        next(request,emitter);
    }

}
