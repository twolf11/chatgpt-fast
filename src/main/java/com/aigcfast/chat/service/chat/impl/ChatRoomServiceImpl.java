
package com.aigcfast.chat.service.chat.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.aigcfast.chat.domain.entity.chat.ChatRoom;
import com.aigcfast.chat.mapper.chat.ChatRoomMapper;
import com.aigcfast.chat.service.chat.ChatRoomService;
import com.aigcfast.chat.util.Tools;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 聊天室表service实现
 * @Author lcy
 * @Date 2023-07-12
 */
@Service
@AllArgsConstructor
@Slf4j
public class ChatRoomServiceImpl extends ServiceImpl<ChatRoomMapper,ChatRoom> implements ChatRoomService {

    @Override public ChatRoom initChatRoot(Integer userId,Long roomId,String messageId,String title){
        ChatRoom chatRoom = ChatRoom.builder().id(Tools.isNotEmpty(roomId) ? roomId : IdWorker.getId())
                .messageId(messageId)
                .createBy(userId)
                .createTime(LocalDateTime.now())
                .build();
        if(Tools.isEmpty(title)){
            chatRoom.setTitle("");
        }else {
            chatRoom.setTitle(title.length() > 1000 ? title.substring(0,1000) : title);
        }
        save(chatRoom);
        return chatRoom;
    }

    @Override public boolean close(Long roomId){
        ChatRoom chatRoom = ChatRoom.builder().id(roomId).status(2).build();
        return updateById(chatRoom);
    }
}
