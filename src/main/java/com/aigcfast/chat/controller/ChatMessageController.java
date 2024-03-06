package com.aigcfast.chat.controller;

import com.aigcfast.chat.common.exception.ServiceException;
import com.aigcfast.chat.common.response.Result;
import com.aigcfast.chat.config.ChatConfig;
import com.aigcfast.chat.service.blackList.BlackListService;
import com.aigcfast.chat.service.chat.WebTokenSelectService;
import com.aigcfast.chat.util.UserUtil;
import com.aigcfast.chat.util.WebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import com.aigcfast.chat.domain.request.chat.ChatMessageRequest;
import com.aigcfast.chat.domain.request.chat.ChatProcessRequest;
import com.aigcfast.chat.service.strategy.chain.OpenAiChatEmitterChain;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

/**
 * 聊天相关接口
 * @Author lcy
 * @Date 2022/5/6 16:20
 */
@AllArgsConstructor
@Tag(name = "聊天相关接口")
@RestController
@RequestMapping("/chat_message")
public class ChatMessageController {


    private  static  final Logger logger = LoggerFactory.getLogger(ChatMessageController.class);


    @Autowired
    private BlackListService blackListService;

    @Autowired
    private WebTokenSelectService webTokenSelectService;


    @Autowired
    private ChatConfig config;

    @Operation(summary = "发送消息")
    @PostMapping("/send")
    public ResponseBodyEmitter sendMessage(@RequestBody @Validated ChatProcessRequest chatProcessRequest,HttpServletResponse response){
        Integer userId = UserUtil.getUserId();
        String ip = WebUtil.getIp();
        if(blackListService.check(userId)){
            blackListService.addBlackIp(ip);
            throw new ServiceException(200,"非法请求！请访问，https://gpt.aigcfast.com  , Q群487850825");
        }
        if(blackListService.checkIp(ip)){
            throw new ServiceException(200,"异常流量!请联系管理员，Q群487850825 请访问，https://gpt.aigcfast.com ");
        }
        logger.info(userId+" ip:"+ip+" 发起请求");
        ChatMessageRequest request = chatProcessRequest.toNewRequest();
        // 超时时间设置 3 分钟
        request.setFast(config.isApiOnly());
        ResponseBodyEmitter emitter = new ResponseBodyEmitter(3 * 60 * 1000L);
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        OpenAiChatEmitterChain openAiChatEmitterChain = new OpenAiChatEmitterChain();
        openAiChatEmitterChain.doChain(request,emitter);

        return emitter;
    }
}
