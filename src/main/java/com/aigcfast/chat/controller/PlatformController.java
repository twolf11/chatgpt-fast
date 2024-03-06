package com.aigcfast.chat.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aigcfast.chat.common.response.Result;
import com.aigcfast.chat.service.platform.PlatformCdkeyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

/**
 * 平台相关接口
 * @Author lcy
 * @Date 2022/5/6 16:20
 */
@AllArgsConstructor
@Tag(name = "平台相关接口")
@RestController
@RequestMapping("/platform")
public class PlatformController {

    private final PlatformCdkeyService cdkeyService;

    @Operation(summary = "生成cdkey")
    @PostMapping("/generateCdkey")
    public Result<String> generateCdkey(@RequestParam Integer number){
        return Result.success(cdkeyService.generateCdkey(number));
    }

    @Operation(summary = "兑换cdkey")
    @PostMapping("/exchangeCdkey")
    public Result<Boolean> exchangeCdkey(@RequestParam String cdkey){
        return Result.success(cdkeyService.exchangeCdkey(cdkey));
    }
}
