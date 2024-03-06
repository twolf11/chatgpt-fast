package com.aigcfast.chat.domain.entity.chat;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 聊天室表
 * @Author lcy
 * @Date 2023-07-12
 */
@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName("chat_room")
public class ChatRoom implements Serializable {

    /** 主键 */
    private Long id;

    /** 第一条消息 */
    private String messageId;

    /** 对话标题，从第一条消息截取 */
    private String title;

    /** 聊天室状态;1-开启，2-关闭 */
    private Integer status;

    /** 用户 id */
    private Integer createBy;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;

}
