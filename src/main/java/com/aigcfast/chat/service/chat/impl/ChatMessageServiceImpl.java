
package com.aigcfast.chat.service.chat.impl;

import com.aigcfast.chat.service.chat.WebTokenSelectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import com.aigcfast.chat.common.enums.ChatMessageTypeEnum;
import com.aigcfast.chat.domain.entity.chat.ChatMessage;
import com.aigcfast.chat.domain.entity.chat.ChatRoom;
import com.aigcfast.chat.mapper.chat.ChatMessageMapper;
import com.aigcfast.chat.service.chat.ChatMessageService;
import com.aigcfast.chat.service.chat.ChatRoomService;
import com.aigcfast.chat.service.sys.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

/**
 * 聊天消息表service实现
 * @Author lcy
 * @Date 2023-07-12
 */
@Service
@AllArgsConstructor
@Slf4j
public class ChatMessageServiceImpl extends ServiceImpl<ChatMessageMapper,ChatMessage> implements ChatMessageService {


    private static final Logger logger = LoggerFactory.getLogger(ChatMessageServiceImpl.class);

    private ChatRoomService chatRoomService;

    private TransactionTemplate transactionTemplate;

    private ThreadPoolTaskExecutor executor;

    private SysUserService sysUserService;

    private RedisTemplate<String,Integer> redisTemplate;

    private WebTokenSelectService webTokenSelectService;


    @Transactional(rollbackFor = Exception.class)
    @Override public void saveMessage(ChatMessage chatMessage,String replyMessageId,String replyContent,String receivedOriginalData){
        //String cacheKey = USER_NUMBER_KEY + ":" + chatMessage.getCreateBy();
        ////设置redis使用缓存
        //Integer number = redisTemplate.opsForValue().get(cacheKey);
        //ModelEnum modelEnum = ModelEnum.getByName(chatMessage.getModelName());
        //int deductionNumber = number != null ? (number > modelEnum.getNumber() ? number - modelEnum.getNumber() : 0) : 0;
        //redisTemplate.opsForValue().set(cacheKey,deductionNumber);

        executor.submit(() -> {
            //SysUser sysUser = SysUser.builder().id(chatMessage.getCreateBy()).memberNumber(deductionNumber).build();
            //sysUserService.updateById(sysUser);
            transactionTemplate.execute(status -> {
                //如果聊天室不存在则创建
                ChatRoom chatRoom = chatRoomService.getById(chatMessage.getChatRoomId());
                if (chatRoom == null) {
                    chatRoomService.initChatRoot(chatMessage.getCreateBy(),chatMessage.getChatRoomId(),chatMessage.getMessageId(),chatMessage.getContent());
                }
                //更新问题消息
                save(chatMessage);
                //更新缓存会话id最后一次响应回来的id
                webTokenSelectService.setConversationLastToken(chatMessage.getConversationId(),chatMessage.getApiKey());
                //组装响应内容
                ChatMessage replyChatMessage = ChatMessage.builder()
                        .messageId(UUID.randomUUID().toString())
                        .ip(chatMessage.getIp())
                        .messageType(ChatMessageTypeEnum.ANSWER)
                        .content(replyContent)
                        .modelName(chatMessage.getModelName())
                        //问题消息为父消息
                        .parentMessageId(replyMessageId)
                        .conversationId(chatMessage.getParentMessageId())
                        .createBy(chatMessage.getCreateBy())
                        .chatRoomId(chatMessage.getChatRoomId())
                        .apiKey(chatMessage.getApiKey())
                        .apiType(chatMessage.getApiType())
                        .originalData(receivedOriginalData)
                        .build();
                logger.info("用户:{},问题:{}",chatMessage.getCreateBy(),chatMessage.getContent());
                save(replyChatMessage);
                return true;
            });
        });

    }
}
