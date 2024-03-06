package com.aigcfast.chat.openai.client;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import com.aigcfast.chat.config.ChatConfig;
import com.aigcfast.chat.domain.dto.chat.ConversationDto;
import com.aigcfast.chat.util.JsonUtil;
import com.aigcfast.chat.util.Tools;
import jakarta.annotation.PostConstruct;
import okhttp3.sse.EventSourceListener;
import okhttp3.sse.EventSources;

/**
 * accessToken客户端
 * @Author lcy
 * @Date 2023/7/14 20:49
 */
@Component
public class WebApiClient {

    private OkHttpClient okHttpClient;

    @Autowired
    private ChatConfig chatConfig;

    static  final  Logger logger = LoggerFactory.getLogger(WebApiClient.class);


    @PostConstruct
    public void init(){
        //初始化okhttp客户端参数
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(chatConfig.getTimeout(),TimeUnit.MILLISECONDS)
                .readTimeout(chatConfig.getTimeout(),TimeUnit.MILLISECONDS)
                .writeTimeout(chatConfig.getTimeout(),TimeUnit.MILLISECONDS)
                .cookieJar(new CookieJar() {
                    private final HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();
                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        cookieStore.put(url, cookies);
                    }
                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        List<Cookie> cookies = cookieStore.get(url);
                        return cookies != null ? cookies : new ArrayList<>();
                    }
                });
        Proxy proxy = getProxy();
        if (proxy != null) {
            builder.proxy(proxy);
        }
        okHttpClient = builder.build();
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

    /**
     * 构建流请求
     * @param accessToken         token
     * @param conversationDto     请求参数
     * @param eventSourceListener 监听器
     * @author lcy
     * @date 2023/8/9 19:42
     **/
    public void streamChatCompletions(String accessToken,ConversationDto conversationDto,EventSourceListener eventSourceListener){


        // 构建请求头
        Headers headers = new Headers.Builder()
                .add(Header.AUTHORIZATION.name(), "Bearer ".concat(accessToken))
                .add(Header.ACCEPT.name(), "text/event-stream")
                .add(Header.CONTENT_TYPE.name(), ContentType.JSON.getValue())
                .build();

        // 构建 Request
        Request request = new Request.Builder()
                .url(chatConfig.getTokenProxyUrl())
                .post(RequestBody.create(JsonUtil.objectToJson(conversationDto), MediaType.parse(ContentType.JSON.getValue())))
                .headers(headers)
                .build();
        //发起请求
        logger.info("webToken发起请求："+chatConfig.getTokenProxyUrl());
        EventSources.createFactory(okHttpClient).newEventSource(request, eventSourceListener);
    }
}

