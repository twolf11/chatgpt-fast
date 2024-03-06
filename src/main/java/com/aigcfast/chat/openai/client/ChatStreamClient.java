package com.aigcfast.chat.openai.client;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.hutool.extra.spring.SpringUtil;
import com.aigcfast.chat.config.ChatConfig;
import com.aigcfast.chat.util.Tools;
import com.unfbx.chatgpt.OpenAiStreamClient;
import jakarta.annotation.PostConstruct;
import okhttp3.OkHttpClient;

/**
 * chatGpt客户端
 * @Author lcy
 * @Date 2023/7/14 20:49
 */
@Component
public class ChatStreamClient {

    private OkHttpClient okHttpClient;

    @Autowired
    private ChatConfig chatConfig;

    static  final Logger logger = LoggerFactory.getLogger(ChatStreamClient.class);


    @PostConstruct
    public void initClient(){
        //初始化okhttp客户端参数
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(chatConfig.getTimeout(),TimeUnit.MILLISECONDS)
                .readTimeout(chatConfig.getTimeout(),TimeUnit.MILLISECONDS)
                .writeTimeout(chatConfig.getTimeout(),TimeUnit.MILLISECONDS);
        Proxy proxy = getProxy();
//        if (proxy != null) {
//            builder.proxy(proxy);
//        }
        okHttpClient = builder.build();
    }

    /**
     * 构建Api客户端
     * @param apiKey apiKey
     * @return com.plexpt.chatgpt.ChatGPTStream
     * @author lcy
     * @date 2023/7/14 20:51
     **/
    public OpenAiStreamClient buildStreamClient(String apiKey){
        return OpenAiStreamClient.builder()
                .okHttpClient(okHttpClient)
                .apiKey(Collections.singletonList(apiKey))
                .apiHost(chatConfig.getOpenaiApiUrl())
                .build();
    }

    /**
     * 构建Api客户端
     * @param apiKeys apiKey列表
     * @return com.plexpt.chatgpt.ChatGPTStream
     * @author lcy
     * @date 2023/7/14 20:51
     **/
    public OpenAiStreamClient buildStreamClient(List<String> apiKeys){
        return OpenAiStreamClient.builder()
                .okHttpClient(okHttpClient)
                .apiKey(apiKeys)
                .apiHost(chatConfig.getOpenaiApiUrl())
                .build();
    }

    /**
     * 获取代理信息
     * @return java.net.Proxy
     * @author lcy
     * @date 2023/7/14 20:48
     **/
    private Proxy getProxy(){
        ChatConfig chatConfig = SpringUtil.getBean(ChatConfig.class);
        // 国内需要代理
        Proxy proxy = Proxy.NO_PROXY;
        if (Tools.isNotEmpty(chatConfig.getHttpProxyHost()) && Tools.isNotEmpty(chatConfig.getHttpProxyPort())) {
            proxy = new Proxy(Proxy.Type.HTTP,new InetSocketAddress(chatConfig.getHttpProxyHost(),chatConfig.getHttpProxyPort()));
        }
        return proxy;
    }
}
