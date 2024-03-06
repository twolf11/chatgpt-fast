package com.aigcfast.chat.service.strategy.chat;

import cn.hutool.extra.spring.SpringUtil;
import com.aigcfast.chat.common.enums.ApiTypeEnum;
import com.aigcfast.chat.domain.request.chat.ChatMessageRequest;
import com.aigcfast.chat.service.chat.ApiInfo;
import com.aigcfast.chat.util.UserUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * 发送消息工厂
 */
@Slf4j
public abstract class ApiInfoFactory {

    protected AbstractOpenaiStrategy abstractOpenaiStrategy;

    static private UserRecordLimit userRecordLimit = new UserRecordLimit();


    public static ApiInfo get(ChatMessageRequest request) {
        Integer userId = UserUtil.getUserId();
        Boolean fast = request.getFast();
        if (fast == null) {
            fast = false;
        }
        if (fast) {
            //超过每日限制的话不允许使用
            //fast = userRecordLimit.canRecord(userId.toString());
        }
        AbstractOpenaiStrategy abstractOpenaiStrategy = null;
        ApiInfo apiInfo = new ApiInfo();
        if (fast) {
            log.info("使用api方式");
            apiInfo.setApiTypeEnum(ApiTypeEnum.API_KEY);
            abstractOpenaiStrategy = SpringUtil.getBean(ApiKeyOpenaiStrategy.class);
            apiInfo = abstractOpenaiStrategy.getApiKey(userId.toString(), "", request);
        } else {
            log.info("使用web api方式");
            apiInfo.setApiTypeEnum(ApiTypeEnum.WEB_API);
            abstractOpenaiStrategy = SpringUtil.getBean(WebApiOpenAiStrategy.class);
            apiInfo = abstractOpenaiStrategy.getApiKey(userId.toString(), "", request);
            if (apiInfo == null) {
                log.info("使用api方式");
                apiInfo = new ApiInfo();
                apiInfo.setApiTypeEnum(ApiTypeEnum.API_KEY);
                abstractOpenaiStrategy = SpringUtil.getBean(ApiKeyOpenaiStrategy.class);
                apiInfo = abstractOpenaiStrategy.getApiKey(userId.toString(), "", request);
            }
        }
        log.info(apiInfo.getKey());
        apiInfo.setAbstractOpenaiStrategy(abstractOpenaiStrategy);
        return apiInfo;
    }

}


//todo  gpt生成的好像可以直接用试试
class UserRecordLimit {
    private Map<String, Integer> userRecordCount = new HashMap<>();
    private Map<String, Long> userRecordTimestamp = new HashMap<>();
    private int maxRecordCount = 20;
    private long recordWindowMillis = 24 * 60 * 60 * 1000; // 24小时的毫秒数

    public boolean canRecord(String userId) {
        long currentTimeMillis = System.currentTimeMillis();

        if (userRecordCount.containsKey(userId) && userRecordTimestamp.containsKey(userId)) {
            long lastRecordTime = userRecordTimestamp.get(userId);
            if (currentTimeMillis - lastRecordTime < recordWindowMillis) {
                // 用户在24小时内已经记录过
                if (userRecordCount.get(userId) < maxRecordCount) {
                    // 用户记录次数未达到上限
                    userRecordCount.put(userId, userRecordCount.get(userId) + 1);
                    userRecordTimestamp.put(userId, currentTimeMillis);
                    return true;
                } else {
                    // 用户记录次数已达上限
                    return false;
                }
            } else {
                // 用户记录时间窗口已过，重置记录次数和时间戳
                userRecordCount.put(userId, 1);
                userRecordTimestamp.put(userId, currentTimeMillis);
                return true;
            }
        } else {
            // 用户第一次记录
            userRecordCount.put(userId, 1);
            userRecordTimestamp.put(userId, currentTimeMillis);
            return true;
        }
    }

    public static void main(String[] args) {
        UserRecordLimit userRecordLimit = new UserRecordLimit();
        String userId = "exampleUser";

        for (int i = 0; i < 25; i++) {
            boolean canRecord = userRecordLimit.canRecord(userId);
            System.out.println("用户是否能记录：" + canRecord);
        }
    }
}
