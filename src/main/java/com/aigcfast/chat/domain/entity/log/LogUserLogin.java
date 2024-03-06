package com.aigcfast.chat.domain.entity.log;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.aigcfast.chat.common.enums.UserRegisterTypeEnum;
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
 * 前端用户登录日志表
 * @Author lcy
 * @Date 2023-07-12
 */
@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName("log_user_login")
public class LogUserLogin implements Serializable {

    /** 主键 */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /** 登录的用户ID */
    private Integer userId;

    /** 登录方式（注册方式），邮箱登录，手机登录等等 */
    private UserRegisterTypeEnum loginType;

    /** 登录的用户名 */
    private String loginUsername;

    /** 登录的IP地址 */
    private String loginIp;

    /** 登录状态，1登录成功，0登录失败 */
    private Boolean loginStatus;

    /** 登录结果 */
    private String message;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

}
