package com.aigcfast.chat.domain.entity.log;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.aigcfast.chat.common.enums.EmailBizTypeEnum;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 邮箱发送日志
 * @Author lcy
 * @Date 2023-07-12
 */
@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName("log_email_send")
public class LogEmailSend implements Serializable {

    /** 主键 */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 发件人邮箱 */
    private String fromEmailAddress;

    /** 收件人邮箱 */
    private String toEmailAddress;

    /** 业务类型 */
    private EmailBizTypeEnum bizType;

    /** 请求 ip */
    private String requestIp;

    /** 发送内容 */
    private String content;

    /** 发送后会返回一个messageId */
    private String messageId;

    /** 发送状态，0失败，1成功 */
    private Integer status;

    /** 发送后的消息，用于记录成功/失败的信息，成功默认为success */
    private String message;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

}
