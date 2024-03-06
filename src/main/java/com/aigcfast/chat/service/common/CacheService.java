package com.aigcfast.chat.service.common;

import com.aigcfast.chat.common.constant.RedisConstant;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 缓存service
 * @Author lcy
 * @Date 2023/6/1 19:14
 */
public interface CacheService<T> extends IService<T> {

    /**
     * 根据value获取key，默认取表ID
     * @param value value
     * @author lcy
     * @date 2023/6/1 19:27
     **/
    Object getKey(T value);

    /**
     * 根据value获取缓存前缀
     * @author lcy
     * @date 2023/6/1 19:27
     **/
    default String getCacheName(){
        return RedisConstant.DEFAULT_CACHE_NAME;
    }

    /**
     * 添加缓存
     * @param value value
     * @author lcy
     * @date 2023/6/1 19:16
     **/
    void addCache(T value);

    /**
     * 添加缓存
     * @param key key
     * @author lcy
     * @date 2023/6/1 19:16
     **/
    T getCache(Object key);

    /**
     * 删除缓存
     * @param key key
     * @author lcy
     * @date 2023/6/1 19:16
     **/
    void deleteCache(Object key);

    /**
     * 重新加载缓存
     * @author lcy
     * @date 2023/7/12 14:27
     **/
    void reloadCache();

}
