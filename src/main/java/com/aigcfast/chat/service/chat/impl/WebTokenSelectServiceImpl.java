package com.aigcfast.chat.service.chat.impl;

import com.aigcfast.chat.common.enums.ApiTypeEnum;
import com.aigcfast.chat.common.exception.ServiceException;
import com.aigcfast.chat.service.chat.ApiInfo;
import com.aigcfast.chat.service.chat.WebTokenSelectService;
import com.aigcfast.chat.util.RedisService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Slf4j
@Service
@AllArgsConstructor
public class WebTokenSelectServiceImpl implements WebTokenSelectService {

    private static Logger logger = LoggerFactory.getLogger(WebTokenSelectServiceImpl.class);


    /**
     * 用户上次使用token
     * p1  用户id
     */
    private static final String WEB_TOKEN_USER_USE_TOKEN = "webToken:lastUseToken:%s";

    /**
     * webToken 可用列表
     * p1 tokenId
     */
    private static final String WEB_TOKEN_LIST = "webToken:list";


    /**
     * 所有token列表此列表只会增加不会减少除非过期
     */
    private static final String WEB_TOKEN_ALL_LIST = "webToken:alllist";


    /**
     * 锁定列表
     * p1 token
     */
    private static final String WEB_TOKEN_LOCK_LIST = "webToken:lockList:%s";


    /**
     * token使用次数记录
     * p1 token
     */
    private static final String WEB_TOKEN_USE_COUNT = "webToken:useCount:%s";


    /**
     * 当前会话使用的token
     * p1  会话id
     */
    private static final String WEB_TOKEN_CONVERSATION = "webToken:conversationMapToken:%s";


    /**
     * token 冷却期列表
     * p1 token
     */
    private static final String WEB_TOKEN_CD_LIST = "webToken:cdList:%s";

    private static final Long MAX_USE_COUNT = 360L;

    private static final int CD = 3600;

    private static final int LOCK_TIME = 60 * 60 * 3;

    /**
     * 会话过期时间
     */
    private static final int CONVERSATION_EXPI_TIME = 3600 * 24 * 15;


    /**
     * 临时锁定用户5秒
     */
    private static final int LOCK_USER_TIME = 5000;

    private RedisService redisService;

    private Map<String,Long>  lockMap = new HashMap<>();

    @Override
    public void lockUser(String userId) {
        lockMap.put(userId,System.currentTimeMillis());
    }

    @Override
    public void releaseUser(String userId) {
        lockMap.remove(userId);
    }

    /**
     * 一人一token
     * 优先已选择过的token
     * 由于达到上限1小时请求80次则进入CD时间(3600秒),切换token 后清除上下文信息(用户体验可能是丢失上下文貌似没什么问题)
     * 频繁获取时增加token霸占时间上限（尽量让同一个用户使用同一个token）
     * 获取token后 token进入待释放倒计时，同个用户再次获取时增加释放倒计时，倒计时未到之前只能由上一个用户获取，倒计时结束
     */
    @Override
    public ApiInfo select(String userId, String conversationId) {
        ApiInfo apiInfo = new ApiInfo();
        Long perTime = lockMap.get(userId);
        if(perTime == null){
            lockMap.put(userId,System.currentTimeMillis());
        }else{
            if(System.currentTimeMillis() - perTime < LOCK_USER_TIME){
                //临时锁定中无法再次获取token
                throw new ServiceException("频繁请求!请5秒后再试!");
            }
        }
        apiInfo.setModelName(ApiTypeEnum.WEB_API.getDefautModelName());
        apiInfo.setApiTypeEnum(ApiTypeEnum.WEB_API);
        //获取当前会话最后一次使用的token
        String lastTokenByConversation = getLastTokenByConversationId(conversationId);
        //判断上次使用的是否已经过期/进入冷却期/已经被锁定
        if (lastTokenByConversation != null) {
            if (checkAvailability(userId, lastTokenByConversation) == false) {
                logger.info("会话上次使用Token不可用重新选择");
                apiInfo = getToken(userId, apiInfo);
                if(apiInfo == null){
                    return  null;
                }
                apiInfo.setChangeKey(true);
            } else {
                //可用,重置锁定时间
                lockToken(lastTokenByConversation, userId);
                logger.info("使用上次会话已记录userId:{}, Token:{}",userId,lastTokenByConversation.hashCode());
                apiInfo.setKey(lastTokenByConversation);
                incurUseCount(lastTokenByConversation);
                return apiInfo;
            }
        } else {
            //新开的会话优先使用最后一次使用的token
            //先获取上次使用的id
            String lastUseTokenKey = String.format(WEB_TOKEN_USER_USE_TOKEN, userId);
            String lastUseToken = redisService.get(lastUseTokenKey);
            if(lastUseToken == null){
                //新会话新分配token
                return getToken(userId,apiInfo);
            }
            if (checkAvailability(userId, lastUseToken) == false) {
                logger.info("最后使用Token不可用删除最后使用记录,重新选择");
                apiInfo = getToken(userId,apiInfo);
                apiInfo.setChangeKey(true);
                lastUseToken = null;
                redisService.delete(lastUseTokenKey);
                String lockKey = String.format(WEB_TOKEN_LOCK_LIST, lastUseToken);
                redisService.delete(lockKey);
            } else {
                //可用,重置锁定时间
                lockToken(lastUseToken, userId);
                logger.info("使用上次已记录,userId:{}, Token:{}", userId,lastUseToken.hashCode());
                apiInfo.setKey(lastUseToken);
                incurUseCount(lastTokenByConversation);
                return apiInfo;
            }
        }
        return apiInfo;
    }

    private ApiInfo getToken(String userId, ApiInfo apiInfo) {
        String token = redisService.leftPop(WEB_TOKEN_LIST);
        if (token == null) {
            logger.warn("当前无Token可分配!");
            return  null;
            //如果没有合适的token分配了，直接return
        }
        incurUseCount(token);
        logger.info("userId:{} , 分配token:{} ",  userId,token.hashCode());
        // 创建一个延迟任务释放token重新回归可用列表
        apiInfo.setKey(token);
        incurUseCount(token);
        lockToken(token, userId);
        return apiInfo;
    }

    /**
     * 锁定token，创建定时任务释放token
     *
     * @param token
     */
    private void lockToken(String token, String userId) {
        String lockKey = String.format(WEB_TOKEN_LOCK_LIST, token);
        redisService.set(lockKey, token, LOCK_TIME);
        String lastKey = String.format(WEB_TOKEN_USER_USE_TOKEN, userId);
        redisService.set(lastKey, token);
    }


    /**
     * 检查是否可用
     * token 是否在可用列表
     * token 在不在冷却期
     * token 是否被当前用户锁定
     *
     * @param token
     */
    private boolean checkAvailability(String userId, String token) {
        //todo 优先检查token是否有效15天
        String webTokenCdKey = String.format(WEB_TOKEN_CD_LIST, token);
        Object cdTime = redisService.get(webTokenCdKey);
        if (cdTime != null) {
            //处于cd期，不可用
            return false;
        }

        List<Object> forList = redisService.getForList(WEB_TOKEN_LIST);
        if (forList.contains(token)) {
            //处于可用列表,可用
            return true;
        }

        Object inLock = redisService.get(String.format(WEB_TOKEN_USER_USE_TOKEN, userId));
        if (inLock != null && token.equals(inLock.toString())) {
            //这个token被当前用户锁定着,可用
            return true;
        }
        return false;
    }


    private void incurUseCount(String token) {
        String webTokenUseCountKey = String.format(WEB_TOKEN_USE_COUNT, token);
        Long increment = redisService.increment(webTokenUseCountKey);
        if (increment >= MAX_USE_COUNT) {
            //token进入CD期
            entersCd(token);
        }
    }

    /**
     * token 进入CD期
     * @param token
     */
    public void entersCd(String token){
        String webTokenUseCountKey = String.format(WEB_TOKEN_USE_COUNT, token);
        redisService.set(String.format(WEB_TOKEN_CD_LIST, token), 3600, CD, TimeUnit.SECONDS);
        redisService.delete(webTokenUseCountKey);
    }


    @Override
    public void initToken() {
        long s = System.currentTimeMillis();
        List<Object> list = redisService.getForList(WEB_TOKEN_ALL_LIST);
        for (Object o : list) {
            Object inCd = redisService.get(String.format(WEB_TOKEN_CD_LIST, o));
            Object inLock = redisService.get(String.format(WEB_TOKEN_LOCK_LIST, o));
            List<Object> forList = redisService.getForList(WEB_TOKEN_LIST);
            if (inCd == null && inLock == null && !forList.contains(o)) {
                logger.info("增加可用token{}", o.hashCode());
                redisService.rightPush(WEB_TOKEN_LIST, o);
            }
        }
        long e = System.currentTimeMillis();
        logger.info("初始token列表耗时："+(e-s));
    }


    @Override
    public void setConversationLastToken(String conversation, String token) {
        String key = String.format(WEB_TOKEN_CONVERSATION, conversation);
        redisService.set(key, token, CONVERSATION_EXPI_TIME, TimeUnit.SECONDS);
    }

    @Override
    public String getLastTokenByConversationId(String conversationId) {
        if(conversationId == null)
            return  null;
        String key = String.format(WEB_TOKEN_CONVERSATION, conversationId);
        return redisService.get(key);
    }

    @Override
    public void reloadToken(List<String> tokens) {
        redisService.delete(WEB_TOKEN_ALL_LIST);
        for (String token : tokens) {
            redisService.rightPush(WEB_TOKEN_ALL_LIST,token);
        }
        this.initToken();
    }
}
