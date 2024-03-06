package com.aigcfast.chat.domain.request.sys;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 用户登录
 * @Author lcy
 * @Date 2023-07-12
 */
@Data
@Schema(title = "用户登录")
public class UserLoginRequest {

    @NotNull
    @Schema(title = "用户名")
    private String username;

    @Schema(title = "密码")
    @NotNull(message = "密码不能为空")
    private String password;
}