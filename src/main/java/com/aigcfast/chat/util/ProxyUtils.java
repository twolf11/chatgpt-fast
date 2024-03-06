package com.aigcfast.chat.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.net.*;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ProxyUtils {

    static  String proxyHost = "tunnel1.docip.net";
    static int proxyPort = 10138;
    // 创建一个OkHttpClient实例，设置代理服务器的用户名和密码

    public static void main(String[] args) {
        // 代理服务器的地址和端口号


        // 代理服务器的用户名和密码
        String username = "suxi123456-rotate";
        String password = "suxi123456";



//                .addInterceptor(chain -> {
//                    // 添加代理服务器的基本认证信息到请求头部
//                    String credentials = Credentials.basic(username, password);
//                    Request authenticatedRequest = chain.request()
//                            .newBuilder()
//                            .header("Proxy-Authorization", credentials)
//                            .build();
//                    return chain.proceed(authenticatedRequest);
//                })
                //.build();


        for (int i = 0; i < 30; i++) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .callTimeout(2, TimeUnit.SECONDS)
                    .proxy(new Proxy(Proxy.Type.HTTP,
                            new InetSocketAddress(proxyHost, proxyPort)))
                    .build();
            Request request = new Request.Builder()
                    //.url("https://ai.fakeopen.com/api/conversation")
                    //.url("http://ifconfig.me/ip")
                    .url("https://ai.fakeopen.com/api/conversation")
                    //.url("http://test.aigcfast.com")
                    .build();
            try {
                // 发送请求并获取响应
                Response response = client.newCall(request).execute();
                System.out.println(response.body().string());
                // 处理响应...
            } catch (IOException e) {
            }
        }
        // 创建一个Request对象

    }
}
