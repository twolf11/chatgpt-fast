package com.aigcfast.chat.service.chat;

import com.aigcfast.chat.domain.entity.chat.ChatMessage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 聊天消息表service
 * @Author lcy
 * @Date 2023-07-12
 */
public interface ChatMessageService extends IService<ChatMessage> {

    /**
     * 保存消息
     * @param chatMessage          问题参数
     * @param replyMessageId       响应消息id
     * @param replyContent         回复内容
     * @param receivedOriginalData 回复原始内容
     * @author lcy
     * @date 2023/7/14 17:54
     **/
    void saveMessage(ChatMessage chatMessage,String replyMessageId,String replyContent,String receivedOriginalData);

}
