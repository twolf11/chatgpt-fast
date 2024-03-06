package com.aigcfast.chat.service.common.impl;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;

import com.aigcfast.chat.util.Tools;
import com.aigcfast.chat.service.common.CacheService;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;

/**
 * 缓存实现
 * @Author lcy
 * @Date 2023/6/1 19:14
 */
public class CacheServiceImpl<M extends BaseMapper<T>,T> extends ServiceImpl<M,T> implements CacheService<T> {

    @Resource
    protected RedisTemplate<Object,T> redisTemplate;

    @Override public Object getKey(T value){
        return "";
    }

    @Override public void addCache(T value){
        if (value == null) {
            return;
        }
        String cacheKey = getCacheName() + ":" + getKey(value);
        redisTemplate.opsForValue().setIfAbsent(cacheKey,value,30,TimeUnit.DAYS);
    }

    @Override public T getCache(Object key){
        return redisTemplate.opsForValue().get(getCacheName() + ":" + key);
    }

    @Override public void deleteCache(Object key){
        redisTemplate.delete(getCacheName() + ":" + key);
    }

    @Override public void reloadCache(){
        //删除旧数据
        Set<Object> keys = redisTemplate.keys(getCacheName() + "*");
        if (Tools.isNotEmpty(keys)) {
            keys.forEach(userKey -> redisTemplate.delete(userKey));
        }
        long count = count();
        long pageSize = 1000;
        long pageCount = count / pageSize + 1;
        //遍历加载所有数据
        for (int i = 1; i <= pageCount; i++) {
            Page<T> page = new Page<>(i,pageSize);
            page(page);
            page.getRecords().forEach(this :: addCache);
        }
    }

}
