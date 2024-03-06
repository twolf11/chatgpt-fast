package com.aigcfast.chat.service.chat;

import com.aigcfast.chat.domain.entity.chat.ChatRoom;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 聊天室表service
 * @Author lcy
 * @Date 2023-07-12
 */
public interface ChatRoomService extends IService<ChatRoom> {

    /**
     * 新建聊天室
     * @param userId     用户id
     * @param roomId    聊天室Id支持传入，为空自动生成
     * @param messageId 消息id
     * @param title     标题内容
     * @return com.aigcfast.chat.domain.entity.chat.ChatRoom
     * @author lcy
     * @date 2023/7/13 14:24
     **/
    ChatRoom initChatRoot(Integer userId,Long roomId,String messageId,String title);

    /**
     * 关闭聊天室
     * @param roomId 聊天室id
     * @return boolean
     * @author lcy
     * @date 2023/7/13 14:26
     **/
    boolean close(Long roomId);

}
