package com.aigcfast.chat.domain.entity.sys;

import java.io.Serializable;
import java.time.LocalDateTime;

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
 * 用户邀请记录表
 * @Author lcy
 * @Date 2023-07-12
 */
@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_user_invite")
public class SysUserInvite implements Serializable {

    /** id */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /** 用户id */
    private Integer userId;

    /** 用户名 */
    private String userName;

    /** 邀请的用户id */
    private Integer inviteUserId;

    /** 邀请的用户名 */
    private String inviteUserName;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

}
