package com.aigcfast.chat.domain.vo.sys;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * 登录返回信息
 * @Author lcy
 * @Date 2023/7/12 10:46
 */
@Builder
@Data
@Schema(title = "登录返回信息")
public class UserLoginVO {

    @Schema(title = "登录的Token")
    private String token;

    @Schema(title = "用户ID")
    private Integer userId;
}
