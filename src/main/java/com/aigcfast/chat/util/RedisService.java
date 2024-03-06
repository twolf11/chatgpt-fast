package com.aigcfast.chat.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Description redis工具类
 * @Author lcy
 * @Date 2021/4/12 18:07
 */
@Component
@SuppressWarnings(value = {"unchecked","rawtypes"})
public class RedisService {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 添加、修改缓存
     * @param key   key
     * @param value value
     * @return boolean
     * @author lcy
     * @date 2019/11/1 19:50
     **/
    public <K,V> boolean set(K key,V value){
        redisTemplate.opsForValue().set(key,value);
        return true;
    }

    /**
     * 添加、修改缓存并且设置过期时间--原子性操作，但是只有setEX操作，不包含setNX
     * @param key      key
     * @param value    value
     * @param expire   过期时间
     * @param timeUnit 时间单位
     * @return boolean
     * @author lcy
     * @date 2019/11/1 19:50
     **/
    public <K,V> boolean set(K key,V value,long expire,TimeUnit timeUnit){
        redisTemplate.opsForValue().set(key,value,expire,timeUnit);
        return true;
    }

    /**
     * 添加、修改缓存并且设置过期时间--时间单位为：秒
     * @param key    key
     * @param value  value
     * @param expire 过期时间
     * @return boolean
     * @author lcy
     * @date 2019/11/1 19:50
     **/
    public <K,V> boolean set(K key,V value,long expire){
        return set(key,value,expire,TimeUnit.SECONDS);
    }

    /**
     * 添加、修改缓存并且设置过期时间--原子性操作用于分布式加锁操作等。这里包含了sexNX和setEX两个操作
     * @param key      key
     * @param value    value
     * @param expire   过期时间
     * @param timeUnit 时间单位
     * @return boolean
     * @author lcy
     * @date 2019/11/1 19:50
     **/
    public <K,V> boolean setIfAbsent(K key,V value,long expire,TimeUnit timeUnit){
        return Optional.ofNullable(redisTemplate.opsForValue().setIfAbsent(key,value,expire,timeUnit)).orElse(false);
    }

    /**
     * 获取缓存信息
     * @param key key
     * @return V
     * @author lcy
     * @date 2019/11/1 19:50
     **/
    public <K,V> V get(K key){
        ValueOperations<K,V> valueOperations = redisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    /**
     * 对key添加过期时间
     * @param key      key
     * @param expire   过期时间
     * @param timeUnit 时间单位
     * @return boolean
     * @author lcy
     * @date 2019/11/1 19:50
     **/
    public <K> boolean expire(K key,long expire,TimeUnit timeUnit){
        return Optional.ofNullable(redisTemplate.expire(key,expire,timeUnit)).orElse(false);
    }

    /**
     * 获取key的过期时间 默认单位：秒。-2表示不存在，-1表示永久，正数为正常时间。如果获取失败默认返回-2
     * @param key key
     * @return Long
     * @author lcy
     * @date 2019/11/1 19:50
     **/
    public <K> Long getExpire(K key){
        return redisTemplate.getExpire(key);
    }

    /**
     * 根据时间单位，获取key的过期时间 获取key的过期时间。 -2表示不存在，-1表示永久，正数为正常时间。如果获取失败默认返回-2
     * @param key key
     * @return Long
     * @author lcy
     * @date 2019/11/1 19:50
     **/
    public <K> Long getExpire(K key,TimeUnit timeUnit){
        return redisTemplate.getExpire(key,timeUnit);
    }

    /**
     * 删除缓存信息
     * @param key key
     * @return boolean
     * @author lcy
     * @date 2019/11/1 19:50
     **/
    public <K> boolean delete(K key){
        return Optional.ofNullable(redisTemplate.delete(key)).orElse(false);
    }

    /**
     * 将 key 中储存的数字值增一。 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCR 操作。
     * <p>
     * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
     * @param key key
     * @return Long
     * @author lcy
     * @date 2019/11/1 19:50
     **/
    public <K> Long increment(K key){
        return redisTemplate.opsForValue().increment(key);
    }

    /**
     * 将 key 中储存的数字值加上增量
     * @param key       key
     * @param increment 增量
     * @return Long
     * @author lcy
     * @date 2019/11/1 19:50
     **/
    public <K> Long increment(K key,long increment){
        return redisTemplate.opsForValue().increment(key,increment);
    }

    /**
     * 将 key 中储存的数字值加上增量
     * @param key       key
     * @param increment 增量
     * @return Double
     * @author lcy
     * @date 2019/11/1 19:50
     **/
    public <K> Double increment(K key,double increment){
        return redisTemplate.opsForValue().increment(key,increment);
    }

    /**
     * 将 key 中储存的数字值减一。 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCR 操作。
     * <p>
     * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
     * @param key key
     * @return Long
     * @author lcy
     * @date 2019/11/1 19:50
     **/
    public <K> Long decrement(K key){
        return redisTemplate.opsForValue().decrement(key);
    }

    /**
     * 将 key 中储存的数字值减去指定数值
     * @param key       key
     * @param increment 指定数值-减量
     * @return Long
     * @author lcy
     * @date 2019/11/1 19:50
     **/
    public <K> Long decrement(K key,long increment){
        return redisTemplate.opsForValue().decrement(key,increment);
    }

    /**
     * 设置hash存储，包含redis key对应一个hash桶。默认value为key
     * @param key     redis key
     * @param hashKey hashKey
     * @return boolean
     * @author lcy
     * @date 2021/4/20 12:35
     **/
    public <K,HK> boolean setForHash(K key,HK hashKey){
        redisTemplate.opsForHash().put(key,hashKey,hashKey);
        return true;
    }

    /**
     * 设置hash存储，包含redis key对应一个hash桶
     * @param key     redis key
     * @param hashKey hashKey
     * @param value   value
     * @return boolean
     * @author lcy
     * @date 2021/4/20 12:35
     **/
    public <K,HK,HV> boolean setForHash(K key,HK hashKey,HV value){
        redisTemplate.opsForHash().put(key,hashKey,value);
        return true;
    }

    /**
     * 根据redis key和hash key获取值
     * @param key     redis key
     * @param hashKey hashKey
     * @return java.lang.Object
     * @author lcy
     * @date 2021/4/20 12:34
     **/
    public <K,HK,HV> HV getForHash(K key,HK hashKey){
        HashOperations<K,HK,HV> hashOperations = redisTemplate.opsForHash();
        return hashOperations.get(key,hashKey);
    }

    /**
     * 根据redis key删除指定hashkey的值，可以为多个
     * @param key     redis key
     * @param hashKey hashKey
     * @return java.lang.long
     * @author lcy
     * @date 2021/4/20 12:34
     **/
    public <K,HK> long deleteForHash(K key,HK... hashKey){
        return redisTemplate.opsForHash().delete(key,hashKey);
    }

    /**
     * 根据redis key获取hash所有值
     * @param key key
     * @return java.util.Map<java.lang.Object,java.lang.Object>
     * @author lcy
     * @date 2021/4/20 12:33
     **/
    public <K,HK,HV> Map<HK,HV> getForHashEntries(K key){
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 添加数据到set集合
     * @param key   redis key
     * @param value value
     * @return java.lang.long
     * @author lcy
     * @date 2021/4/20 12:34
     **/
    public <K,V> long setForSet(K key,V... value){
        return Optional.ofNullable(redisTemplate.opsForSet().add(key,value)).orElse(0L);
    }

    /**
     * 根据redis key和判断value是否存在set集合中
     * @param key   redis key
     * @param value value
     * @return java.lang.boolean
     * @author lcy
     * @date 2021/4/20 12:34
     **/
    public <K,V> boolean checkInSet(K key,V value){
        return Optional.ofNullable(redisTemplate.opsForSet().isMember(key,value)).orElse(false);
    }

    /**
     * 根据key获取set集合全部内容
     * @param key key
     * @return java.util.Set<V>
     * @author lcy
     * @date 2021/5/17 11:55
     **/
    public <K,V> Set<V> getForSet(K key){
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 根据key获取集合数据条数
     * @param key key
     * @return long
     * @author lcy
     * @date 2021/5/17 11:56
     **/
    public <K> long getSizeForSet(K key){
        return Optional.ofNullable(redisTemplate.opsForSet().size(key)).orElse(0L);
    }

    /**
     * 根据redis key删除set集合指定元素，value可以为多个
     * @param key    key
     * @param values values
     * @return long
     * @author lcy
     * @date 2021/4/20 12:33
     **/
    public <K,V> long removeFromSet(K key,V... values){
        return Optional.ofNullable(redisTemplate.opsForSet().remove(key,values)).orElse(0L);
    }

    /**
     * 根据key获取list集合里的大小
     * @param key key
     * @return java.lang.Long
     * @author lcy
     * @date 2021/4/21 10:54
     **/
    public <K> Long getListSize(K key){
        return redisTemplate.opsForList().size(key);
    }

    /**
     * 在集合的最左边（前面）插入数据，并且返回当前集合的size，插入失败返回-1
     * @param key   key
     * @param value value
     * @return java.lang.Long
     * @author lcy
     * @date 2021/4/21 11:13
     **/
    public <K,V> Long leftPush(K key,V value){
        return redisTemplate.opsForList().leftPush(key,value);
    }

    /**
     * 在某个值的左边（前面）插入数据，把value值放到集合中指定的pivot的前面，如果pivot存在的话。如果不存在则插入失败，并且返回-1
     * @param key   key
     * @param pivot 参考值
     * @param value value
     * @return Long 当前集合size
     * @author lcy
     * @date 2021/4/21 10:59
     **/
    public <K,V> Long leftPush(K key,V pivot,V value){
        return redisTemplate.opsForList().leftPush(key,pivot,value);
    }

    /**
     * 在集合的最左边（前面）插入数据，注意集合的顺序，index在前面的先插入，也就是说如果集合是[1,2,3]，插入以后变成[3,2,1]
     * <p>
     * 返回当前集合的size，-1表示插入失败
     * @param key    key
     * @param values values--集合数据
     * @return java.lang.Long
     * @author lcy
     * @date 2021/4/21 11:13
     **/
    public <K,V> Long leftPushAll(K key,Collection<V> values){
        return redisTemplate.opsForList().leftPushAll(key,values);
    }

    /**
     * 在集合的最右边（后面）插入数据，并且返回当前集合的size，插入失败返回-1
     * @param key   key
     * @param value value
     * @return java.lang.Long
     * @author lcy
     * @date 2021/4/21 11:13
     **/
    public <K,V> Long rightPush(K key,V value){
        return redisTemplate.opsForList().rightPush(key,value);
    }

    /**
     * 在某个值的最右边（后面）插入数据，把value值放到集合中指定的pivot的后面，如果pivot存在的话。如果不存在则插入失败，并且返回-1
     * @param key   key
     * @param pivot 参考值
     * @param value value
     * @return Long 当前集合size
     * @author lcy
     * @date 2021/4/21 10:59
     **/
    public <K,V> Long rightPush(K key,V pivot,V value){
        return redisTemplate.opsForList().leftPush(key,pivot,value);
    }

    /**
     * 在集合的最右边（后面）插入数据，按顺序插入
     * <p>
     * 返回当前集合的size，-1表示插入失败
     * @param key    key
     * @param values values--集合数据
     * @return java.lang.Long
     * @author lcy
     * @date 2021/4/21 11:13
     **/
    public <K,V> Long rightPushAll(K key,Collection<V> values){
        return redisTemplate.opsForList().rightPushAll(key,values);
    }

    /**
     * 从集合的最左边（前面）当中取出元素
     * @param key key
     * @return V
     * @author lcy
     * @date 2021/4/21 12:29
     **/
    public <K,V> V leftPop(K key){
        ListOperations<K,V> listOperations = redisTemplate.opsForList();
        return listOperations.leftPop(key);
    }

    /**
     * 从集合的最右边（后面）当中取出元素
     * @param key key
     * @return V
     * @author lcy
     * @date 2021/4/21 12:29
     **/
    public <K,V> V rightPop(K key){
        ListOperations<K,V> listOperations = redisTemplate.opsForList();
        return listOperations.rightPop(key);
    }

    /**
     * 根据key获取集合数据--因为先查找范围才返回数据，是两个操作，不能保证数据完全准确性，如需要保证数据完整需要通过lua脚本
     * @param key key
     * @return java.util.List<V>
     * @author lcy
     * @date 2021/4/21 11:28
     **/
    public <K,V> List<V> getForList(K key){
        Long size = getListSize(key);
        return size == null || size == 0 ? new ArrayList<>() : getForList(key,0,size);
    }

    /**
     * 根据key，获取list下标范围集合的数据
     * @param key   key
     * @param start 开始下标
     * @param end   结束下标
     * @return java.util.List<V>
     * @author lcy
     * @date 2021/4/21 11:29
     **/
    public <K,V> List<V> getForList(K key,long start,long end){
        return redisTemplate.opsForList().range(key,start,end);
    }

    /**
     * 根据key获取指定集合下标数据，注意：如果不存在会返回null
     * @param key   key
     * @param index 下标
     * @return V
     * @author lcy
     * @date 2021/4/21 11:30
     **/
    public <K,V> V index(K key,long index){
        ListOperations<K,V> listOperations = redisTemplate.opsForList();
        return listOperations.index(key,index);
    }

}
