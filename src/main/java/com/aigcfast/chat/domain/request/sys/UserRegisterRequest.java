package com.aigcfast.chat.domain.request.sys;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 用户注册
 * @Author lcy
 * @Date 2023-07-12
 */
@Data
@Schema(title = "前端用户注册")
public class UserRegisterRequest {

    @Size(min = 6, max = 64, message = "用户名长度应为6~64个字符")
    @NotNull
    @Schema(title = "用户名，可以为邮箱，可以为手机号")
    private String identity;

    @Schema(title = "密码")
    @NotNull(message = "密码不能为空")
    private String password;

    @Schema(title = "验证码")
    private String verificationCode;

    @Schema(title = "邀请人id")
    private Integer inviter;
}