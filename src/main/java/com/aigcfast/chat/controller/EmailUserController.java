package com.aigcfast.chat.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.Validator;
import com.aigcfast.chat.common.response.Result;
import com.aigcfast.chat.domain.request.sys.UserLoginRequest;
import com.aigcfast.chat.domain.request.sys.UserRegisterRequest;
import com.aigcfast.chat.domain.vo.sys.UserInfoVO;
import com.aigcfast.chat.domain.vo.sys.UserLoginVO;
import com.aigcfast.chat.service.strategy.user.UserEmailStrategy;
import com.aigcfast.chat.service.sys.SysUserService;
import com.aigcfast.chat.util.AssertUtil;
import com.aigcfast.chat.util.UserUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

/**
 * 邮箱用户相关接口
 * @Author lcy
 * @Date 2022/5/6 16:20
 */
@AllArgsConstructor
@Tag(name = "邮箱用户相关接口")
@RestController
@RequestMapping("/user")
public class EmailUserController {

    private final UserEmailStrategy userEmailStrategy;

    private final SysUserService sysUserService;

    @Operation(summary = "邮箱注册")
    @PostMapping("/register/email")
    public Result<String> register(@Validated @RequestBody UserRegisterRequest request){
        userEmailStrategy.register(request);
        return Result.success("注册成功");
    }

    @Operation(summary = "发送邮箱")
    @GetMapping("/register/sendEmail")
    public Result<String> sendEmail(@Validated @RequestBody String email){
        AssertUtil.isEmpty(!Validator.isEmail(email),"邮箱格式格式不正确");
        userEmailStrategy.sendRegisterVerifyCode(email);
        return Result.success("发送成功");
    }

    @Operation(summary = "用户信息")
    @GetMapping("/info")
    public Result<UserInfoVO> getUserInfo(){
        return Result.success(UserUtil.getUserInfo());
    }

    @Operation(summary = "邮箱登录")
    @PostMapping("/login/email")
    public Result<UserLoginVO> login(@RequestBody UserLoginRequest request){
        return Result.success(userEmailStrategy.login(request));
    }

    @Operation(summary = "邮箱登出")
    @PostMapping("/logout/email")
    public Result<Void> logout(){
        StpUtil.logout();
        return Result.success();
    }

    @Operation(summary = "获取会员次数")
    @GetMapping("/memberNumber")
    public Result<Integer> getMemberNumber(){
        return Result.success(sysUserService.getMemberNumber(UserUtil.getUserId()));
    }

}