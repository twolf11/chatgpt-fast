package com.aigcfast.chat.service.chat.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import com.aigcfast.chat.common.enums.ApiTypeEnum;
import com.aigcfast.chat.service.chat.ApiInfo;
import com.aigcfast.chat.service.chat.WebTokenSelectService;
import com.aigcfast.chat.util.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import cn.hutool.core.collection.CollUtil;
import com.aigcfast.chat.common.enums.ConversationModelEnum;
import com.aigcfast.chat.common.enums.ModelEnum;
import com.aigcfast.chat.domain.entity.sys.SysDict;
import com.aigcfast.chat.service.chat.ApikeyService;
import com.aigcfast.chat.service.sys.SysDictService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * apiKey实现类
 * @author lcy
 */
@Slf4j
@Service
@AllArgsConstructor
public class ApikeyServiceImpl implements ApikeyService, ApplicationRunner {


    private  static  final  Logger logger = LoggerFactory.getLogger(ApikeyService.class);


    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);


    /** API KEY缓存 */
    public final Map<String,List<String>> apiKeyMap = new HashMap<>();

    public final Map<String,AtomicLong> indexMap = new HashMap<>();

    private SysDictService sysDictService;


    private RedisService redisService;

    private WebTokenSelectService webTokenSelectService;


    @Override public void run(ApplicationArguments args){
        reloadApikey();
    }

    @Override public void reloadApikey(){
        log.info("加载API_KEY开始");
        ModelEnum[] modelEnums = ModelEnum.values();
        ConversationModelEnum[] conversationModelEnums = ConversationModelEnum.values();
        List<String> modelTypes = new ArrayList<>(modelEnums.length + conversationModelEnums.length);
        Arrays.stream(modelEnums).map(ModelEnum :: getType).distinct().forEach(modelTypes :: add);
        Arrays.stream(conversationModelEnums).map(ConversationModelEnum :: getType).distinct().forEach(modelTypes :: add);

        apiKeyMap.clear();
        modelTypes.forEach(modelType -> {
            List<String> keys = sysDictService.getByDictType(modelType).stream().map(SysDict :: getDictValue).collect(Collectors.toList());
            apiKeyMap.put(modelType,keys);
            indexMap.put(modelType,new AtomicLong());
            log.info("加载数据：modelType={},apiKey={}",modelType,CollUtil.join(keys,","));
        });
        log.info("加载API_KEY结束");
        scheduler.scheduleAtFixedRate(()->{
            webTokenSelectService.initToken();
        }, 0, 1, TimeUnit.MINUTES);

    }

    @Override public List<String> getApikeyList(String modelType){
        return apiKeyMap.get(modelType);
    }

    @Override public Map<String,List<String>> getAllKeyMap(){
        return apiKeyMap;
    }

    @Override
    public ApiInfo getWebToken(String userId) {
        return null;
    }

    @Override
    public ApiInfo getApikey(String userId,String modelType,String conversationId){
        ApiInfo apiInfo = new ApiInfo();
        String key = null;
        //优先从webToken中获取如果没有可用的再去获取apikey
        apiInfo.setModelName("gpt-3.5-token");
        apiInfo.setApiTypeEnum(ApiTypeEnum.API_KEY);
        List<String> apis = apiKeyMap.get("gpt-3.5-token");
        int index = (int)(System.currentTimeMillis()/1000 % apis.size());
        key = apis.get(index);
        apiInfo.setKey(key);
        return apiInfo;
    }

    @Override public void removeApikey(String modelType,String apiKey){
        List<String> apiKeyList = apiKeyMap.get(modelType);
        apiKeyList.remove(apiKey);
        sysDictService.delete(modelType,apiKey);
    }





}
