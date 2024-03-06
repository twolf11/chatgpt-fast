package com.aigcfast.chat.common.constant;

/**
 * redis常量
 * @Author lcy
 * @Date 2023/6/1 14:37
 */
public class RedisConstant {

    /** 默认缓存名称 */
    public final static String DEFAULT_CACHE_NAME = "default";

    /** 注册验证码 */
    public final static String REGISTER_VERIFY_CODE = "verifyCode:register:%s";

    /** 重复发送验证码限制 */
    public final static String SEND_EMAIL_LIMIT = "email:limit:%s";

    /** 根据用户id存储的缓存key */
    public final static String USER_ID_KEY = "user:base";

    /** 根据邮箱的缓存key */
    public final static String USER_EMAIL_KEY = "user:email";

    /** 用户使用次数缓存key */
    public final static String USER_NUMBER_KEY = "user:number";

    /** 用户会员套餐key */
    public final static String USER_MEMBER_PACKAGE_KEY = "user:memberPackage";

    /** 字典的缓存key */
    public final static String DICT_KEY = "dict";

}
