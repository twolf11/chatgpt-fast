package com.aigcfast.chat.domain.entity.sys;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 前端用户基础信息表
 * @Author lcy
 * @Date 2023-07-12
 */
@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_user")
public class SysUser extends Model<SysDict> {

    /** 主键 */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /** 用户昵称 */
    private String nickname;

    /** 描述 */
    private String description;

    /** 头像 */
    private String avatarUrl;

    /** 上一次登录 IP */
    private String lastLoginIp;

    /** 会员次数 */
    private Integer memberNumber;

    /** 到期时间 */
    private LocalDateTime expireTime;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间 */
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

}
