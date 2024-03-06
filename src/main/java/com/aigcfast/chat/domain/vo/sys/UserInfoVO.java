package com.aigcfast.chat.domain.vo.sys;

import com.aigcfast.chat.common.enums.UserRegisterTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户信息
 * @Author lcy
 * @Date 2023/7/12 10:46
 */
@Builder
@Data
@Schema(title = "用户信息")
public class UserInfoVO {

    @Schema(title = "用户注册类型")
    private UserRegisterTypeEnum userRegisterType;

    @Schema(title = "用户名：如邮箱或手机号")
    private String username;

    @Schema(title = "扩展：如邮箱登录则用邮箱id")
    private Integer extraId;

    @Schema(title = "用户信息")
    private UserVO user;

    /**
     * 用户信息
     * @Author lcy
     * @Date 2023/7/12 10:46
     */
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class UserVO {

        @Schema(title = "用户ID")
        private Integer userId;

        @Schema(title = "用户昵称")
        private String nickname;

        @Schema(title = "描述")
        private String description;

        @Schema(title = "头像地址")
        private String avatarUrl;
    }
}
