package com.aigcfast.chat.util;

import java.util.Collection;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.aigcfast.chat.common.exception.ServiceException;
import com.aigcfast.chat.common.response.ResultCode;
import io.micrometer.common.util.StringUtils;

/**
 * @Description 断言工具，抛出ServiceException
 * @Author lcy
 * @Date 2020/12/7 15:58
 */
public class AssertUtil {

    /**
     * 判断结果为true，抛出异常提示
     * @param expression 布尔值
     * @author lcy
     * @date 2020/12/7 16:03
     **/
    public static void isTrue(boolean expression){
        isTrue(expression,"Assert Exception");
    }

    /**
     * 判断结果为true，抛出异常提示
     * @param expression 布尔值
     * @param message    提示信息
     * @author lcy
     * @date 2020/12/7 16:03
     **/
    public static void isTrue(boolean expression,String message){
        if (expression) {
            throw new ServiceException(message);
        }
    }

    /**
     * 判断字符为空，抛出异常提示
     * @param str     需要判断的字符串
     * @param message 提示信息
     * @author lcy
     * @date 2020/12/7 16:03
     **/
    public static void isEmpty(String str,String message){
        if (StringUtils.isBlank(str)) {
            throw new ServiceException(message);
        }
    }

    /**
     * 判断对象为null，抛出异常提示
     * @param t       需要判断的对象
     * @param message 提示信息
     * @author lcy
     * @date 2020/12/7 16:03
     **/
    public static <T> void isEmpty(T t,String message){
        if (t == null) {
            throw new ServiceException(message);
        }
    }

    /**
     * 判断数组为空，抛出异常提示
     * @param t       需要判断的对象
     * @param message 提示信息
     * @author lcy
     * @date 2020/12/7 16:03
     **/
    public static <T> void isEmpty(T[] t,String message){
        if (t == null || t.length == 0) {
            throw new ServiceException(message);
        }
    }

    /**
     * 判断map为空，抛出异常提示
     * @param map     需要判断的对象
     * @param message 提示信息
     * @author lcy
     * @date 2020/12/7 16:03
     **/
    public static <K,V> void isEmpty(Map<K,V> map,String message){
        if (CollectionUtils.isEmpty(map)) {
            throw new ServiceException(message);
        }
    }

    /**
     * 判断对象为空，抛出异常提示
     * @param collection 集合对象
     * @param message    提示信息
     * @author lcy
     * @date 2020/12/7 16:03
     **/
    public static <T> void isEmpty(Collection<T> collection,String message){
        if (CollectionUtils.isEmpty(collection)) {
            throw new ServiceException(message);
        }
    }

    /**
     * 判断字符不为空，抛出异常提示
     * @param str     需要判断的字符串
     * @param message 提示信息
     * @author lcy
     * @date 2020/12/7 16:03
     **/
    public static void isNotEmpty(String str,String message){
        if (StringUtils.isNotBlank(str)) {
            throw new ServiceException(message);
        }
    }

    /**
     * 判断对象不为null，抛出异常提示
     * @param t       需要判断的对象
     * @param message 提示信息
     * @author lcy
     * @date 2020/12/7 16:03
     **/
    public static <T> void isNotEmpty(T t,String message){
        if (t != null) {
            throw new ServiceException(message);
        }
    }

    /**
     * 判断数组不为空，抛出异常提示
     * @param t       需要判断的对象
     * @param message 提示信息
     * @author lcy
     * @date 2020/12/7 16:03
     **/
    public static <T> void isNotEmpty(T[] t,String message){
        if (t != null) {
            throw new ServiceException(message);
        }
    }

    /**
     * 判断map不为空，抛出异常提示
     * @param map     需要判断的对象
     * @param message 提示信息
     * @author lcy
     * @date 2020/12/7 16:03
     **/
    public static <K,V> void isNotEmpty(Map<K,V> map,String message){
        if (!CollectionUtils.isEmpty(map)) {
            throw new ServiceException(message);
        }
    }

    /**
     * 判断对象不为空，抛出异常提示
     * @param collection 集合对象
     * @param message    提示信息
     * @author lcy
     * @date 2020/12/7 16:03
     **/
    public static <T> void isNotEmpty(Collection<T> collection,String message){
        if (!CollectionUtils.isEmpty(collection)) {
            throw new ServiceException(message);
        }
    }

    /**
     * 判断结果为true，抛出异常提示
     * @param expression 布尔值
     * @param resultCode 结果枚举
     * @author lcy
     * @date 2020/12/7 16:03
     **/
    public static void isTrue(boolean expression,ResultCode resultCode){
        if (expression) {
            throw new ServiceException(resultCode);
        }
    }

    /**
     * 判断字符为空，抛出异常提示
     * @param str        需要判断的字符串
     * @param resultCode 结果枚举
     * @author lcy
     * @date 2020/12/7 16:03
     **/
    public static void isEmpty(String str,ResultCode resultCode){
        if (StringUtils.isBlank(str)) {
            throw new ServiceException(resultCode);
        }
    }

    /**
     * 判断对象为null，抛出异常提示
     * @param t          需要判断的对象
     * @param resultCode 结果枚举
     * @author lcy
     * @date 2020/12/7 16:03
     **/
    public static <T> void isEmpty(T t,ResultCode resultCode){
        if (t == null) {
            throw new ServiceException(resultCode);
        }
    }

    /**
     * 判断数组为空，抛出异常提示
     * @param t          需要判断的对象
     * @param resultCode 结果枚举
     * @author lcy
     * @date 2020/12/7 16:03
     **/
    public static <T> void isEmpty(T[] t,ResultCode resultCode){
        if (t == null || t.length == 0) {
            throw new ServiceException(resultCode);
        }
    }

    /**
     * 判断map为空，抛出异常提示
     * @param map        需要判断的对象
     * @param resultCode 结果枚举
     * @author lcy
     * @date 2020/12/7 16:03
     **/
    public static <K,V> void isEmpty(Map<K,V> map,ResultCode resultCode){
        if (CollectionUtils.isEmpty(map)) {
            throw new ServiceException(resultCode);
        }
    }

    /**
     * 判断对象为空，抛出异常提示
     * @param collection 集合对象
     * @param resultCode 结果枚举
     * @author lcy
     * @date 2020/12/7 16:03
     **/
    public static <T> void isEmpty(Collection<T> collection,ResultCode resultCode){
        if (CollectionUtils.isEmpty(collection)) {
            throw new ServiceException(resultCode);
        }
    }

    /**
     * 判断字符不为空，抛出异常提示
     * @param str        需要判断的字符串
     * @param resultCode 结果枚举
     * @author lcy
     * @date 2020/12/7 16:03
     **/
    public static void isNotEmpty(String str,ResultCode resultCode){
        if (StringUtils.isNotBlank(str)) {
            throw new ServiceException(resultCode);
        }
    }

    /**
     * 判断对象不为null，抛出异常提示
     * @param t          需要判断的对象
     * @param resultCode 结果枚举
     * @author lcy
     * @date 2020/12/7 16:03
     **/
    public static <T> void isNotEmpty(T t,ResultCode resultCode){
        if (t != null) {
            throw new ServiceException(resultCode);
        }
    }

    /**
     * 判断数组不为空，抛出异常提示
     * @param t          需要判断的对象
     * @param resultCode 结果枚举
     * @author lcy
     * @date 2020/12/7 16:03
     **/
    public static <T> void isNotEmpty(T[] t,ResultCode resultCode){
        if (t != null) {
            throw new ServiceException(resultCode);
        }
    }

    /**
     * 判断map不为空，抛出异常提示
     * @param map        需要判断的对象
     * @param resultCode 结果枚举
     * @author lcy
     * @date 2020/12/7 16:03
     **/
    public static <K,V> void isNotEmpty(Map<K,V> map,ResultCode resultCode){
        if (!CollectionUtils.isEmpty(map)) {
            throw new ServiceException(resultCode);
        }
    }

    /**
     * 判断对象不为空，抛出异常提示
     * @param collection 集合对象
     * @param resultCode 结果枚举
     * @author lcy
     * @date 2020/12/7 16:03
     **/
    public static <T> void isNotEmpty(Collection<T> collection,ResultCode resultCode){
        if (!CollectionUtils.isEmpty(collection)) {
            throw new ServiceException(resultCode);
        }
    }

}
