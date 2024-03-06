package com.aigcfast.chat.service.chat;

import java.util.List;
import java.util.Map;

import com.aigcfast.chat.common.enums.IModelType;

/**
 * apiKey服务类
 * @Author lcy
 * @Date 2023/6/1 19:14
 */
public interface ApikeyService {

    /**
     * 加载API_KEY
     * @author lcy
     * @date 2023/6/7 15:05
     **/
    void reloadApikey();

    /**
     * 获取apiKey列表
     * @param modelType 模型名称
     * @return java.util.List<java.lang.String>
     * @author lcy
     * @date 2023/6/14 15:06
     **/
    List<String> getApikeyList(String modelType);

    /**
     * 获取apiKey分类
     * @return java.util.List<java.lang.String>
     * @author lcy
     * @date 2023/6/14 15:06
     **/
    Map<String,List<String>> getAllKeyMap();

    /**
     * 获取单个apiKey--顺序
     * @param modelType 模型类型
     * @return java.util.List<java.lang.String>
     * @author lcy
     * @date 2023/6/14 15:06
     **/
    ApiInfo getApikey(String userId,String modelType,String conversationId);


    ApiInfo getWebToken(String userId);

    /**
     * 删除apiKey
     * @param modelType 模型类型
     * @param apiKey    apiKey
     * @author lcy
     * @date 2023/6/14 15:06
     **/
    void removeApikey(String modelType,String apiKey);

}
