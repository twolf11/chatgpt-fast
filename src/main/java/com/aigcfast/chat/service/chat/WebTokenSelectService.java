package com.aigcfast.chat.service.chat;

import java.util.List;

/**
 * webToken 选择服务类
 */
public interface WebTokenSelectService {

    /**
     * 选择合适的token
     * @param userId
     * @param conversationId 当前会话id
     **/
    ApiInfo select(String userId,String conversationId);

    /**
     * 短时间锁定用户，分配token后短时间内不能再次调用，保证一个用户同一时刻内只能使用同一个token
     * 避免不断的打开新聊天分配token导致，token不足
     * @param userId
     */
    void lockUser(String userId);

    /**
     * 释放用户
     * @param userId
     */
    void releaseUser(String userId);

    void setConversationLastToken(String conversation, String lastToken);


    String getLastTokenByConversationId(String conversationId);

    /**
     * 初始化token
     */
    void initToken();

    /**
     * 重新加载token
     */
    void reloadToken(List<String> tokens);
}
