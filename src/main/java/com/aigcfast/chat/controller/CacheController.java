package com.aigcfast.chat.controller;

import java.util.ArrayList;
import java.util.List;

import com.aigcfast.chat.service.chat.WebTokenSelectService;
import org.springframework.web.bind.annotation.*;

import com.aigcfast.chat.common.response.Result;
import com.aigcfast.chat.service.chat.ApikeyService;
import com.aigcfast.chat.service.common.CacheService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * 缓存相关接口
 * @Author lcy
 * @Date 2022/5/6 16:20
 */
@AllArgsConstructor
@Tag(name = "缓存相关接口")
@RestController
@RequestMapping("/cache")
public class CacheController {

    private final List<CacheService> cacheServices;

    private final ApikeyService apiKeyService;


    private WebTokenSelectService webTokenSelectService;

    @Operation(summary = "重新加载缓存")
    @PostMapping("/reloadToken")
    public Result<String> reloadToken(String tokens){
        List<String> list = new ArrayList<>();
        for (String token : tokens.split("------")) {
            token = token.replaceAll("\\n","");
            if("".equals(token)){
                continue;
            }
            list.add(token);
        }
        webTokenSelectService.reloadToken(list);
        return Result.success();
    }


    @Operation(summary = "重新加载缓存")
    @GetMapping("/reload")
    public Result<String> reload(){
        cacheServices.forEach(CacheService :: reloadCache);
        return Result.success();
    }

    @Operation(summary = "重新加载apikey缓存")
    @GetMapping("/reloadApikey")
    public Result<String> reloadApikey(){
        apiKeyService.reloadApikey();
        return Result.success();
    }

}