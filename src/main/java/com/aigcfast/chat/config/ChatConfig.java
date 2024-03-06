package com.aigcfast.chat.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.aigcfast.chat.util.Tools;
import lombok.Data;

/**
 * 聊天配置
 * @Author lcy
 * @Date 2022/5/6 16:20
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "chat")
public class ChatConfig {

    /**
     * OpenAI API URL
     * @link <a href="https://api.openai.com"/>
     */
    private String openaiApiUrl;

    /**
     * 使用 accessToken 时，不管你是国内还是国外的机器，都会使用代理。默认代理为 pengzhile 大佬的 https://ai.fakeopen.com/api/conversation，这不是后门也不是监听，除非你有能力自己翻过 CF 验证，用前请知悉。社区代理（注意：只有这两个是推荐，其他第三方来源，请自行甄别）
     */
    private String tokenProxyUrl;

    /**
     * 超时毫秒
     */
    private Integer timeout;

    /**
     * HTTP 代理主机
     */
    private String httpProxyHost;

    /**
     * HTTP 代理端口
     */
    private Integer httpProxyPort;

    /**
     * 判断是否有 http 代理
     */
    public Boolean hasHttpProxy(){
        return Tools.isNotEmpty(httpProxyHost) && Tools.isNotEmpty(httpProxyPort);
    }

    private  boolean apiOnly;

}
