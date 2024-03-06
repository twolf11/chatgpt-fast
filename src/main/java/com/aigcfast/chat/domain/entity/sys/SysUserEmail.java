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
 * 前端用户邮箱登录
 * @Author lcy
 * @Date 2023-07-12
 */
@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_user_email")
public class SysUserEmail extends Model<SysDict> {

    /** 主键 */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /** 邮箱账号 */
    private String email;

    /** 加密后的密码 */
    private String password;

    /** 加密盐 */
    private String salt;

    /** 用户表的ID */
    private Integer userId;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间 */
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

}
