
package com.aigcfast.chat.service.sys.impl;

import java.io.Serializable;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import cn.hutool.core.util.RandomUtil;
import com.aigcfast.chat.config.RegisterConfig;
import com.aigcfast.chat.domain.entity.sys.SysUser;
import com.aigcfast.chat.mapper.sys.SysUserMapper;
import com.aigcfast.chat.service.common.impl.CacheServiceImpl;
import com.aigcfast.chat.service.sys.SysUserService;
import com.aigcfast.chat.util.Tools;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.aigcfast.chat.common.constant.RedisConstant.USER_ID_KEY;
import static com.aigcfast.chat.common.constant.RedisConstant.USER_NUMBER_KEY;

/**
 * 前端用户基础信息表service实现
 * @Author lcy
 * @Date 2023-07-12
 */
@Service
@AllArgsConstructor
@Slf4j
public class SysUserServiceImpl extends CacheServiceImpl<SysUserMapper,SysUser> implements SysUserService {

    private final RegisterConfig registerConfig;

    private final RedisTemplate<Object,Object> redisObjectTemplate;

    @Override public Object getKey(SysUser value){
        return value.getId();
    }

    @Override public String getCacheName(){
        return USER_ID_KEY;
    }

    @Override public void addCache(SysUser value){
        super.addCache(value);
        String cacheKey = USER_NUMBER_KEY + ":" + getKey(value);
        //设置redis使用缓存
        redisObjectTemplate.opsForValue().set(cacheKey,value.getMemberNumber());
    }

    @Override public void deleteCache(Object key){
        super.deleteCache(key);
        redisObjectTemplate.delete(USER_NUMBER_KEY + ":" + key);
    }

    @Override public void reloadCache(){
        //删除旧数据
        Set<Object> keys = redisObjectTemplate.keys(getCacheName() + "*");
        if (Tools.isNotEmpty(keys)) {
            keys.forEach(userKey -> redisTemplate.delete(userKey));
        }
        super.reloadCache();
    }

    @Override public boolean save(SysUser entity){
        super.save(entity);
        addCache(entity);
        return true;
    }

    @Override public boolean updateById(SysUser entity){
        boolean b = super.updateById(entity);
        SysUser byId = super.getById(entity.getId());
        addCache(byId);
        return b;
    }

    @Override public SysUser getById(Serializable id){
        SysUser sysUser = getCache(id);
        if (Tools.isEmpty(sysUser)) {
            sysUser = super.getById(id);
            addCache(sysUser);
        }
        return sysUser;
    }

    @Override public boolean removeById(Serializable id){
        deleteCache(id);
        return super.removeById(id);
    }

    @Override public SysUser createUser(){
        SysUser sysUser = SysUser.builder()
                .nickname("GPT_" + RandomUtil.randomString(6))
                .memberNumber(registerConfig.getInitNumber())
                .build();

        this.save(sysUser);

        return sysUser;
    }

    @Override public void updateLastLoginIp(Integer userId,String ip){
        SysUser sysUser = SysUser.builder().id(userId).lastLoginIp(ip).build();
        updateById(sysUser);
    }

    @Override public void extendMemberNumber(Integer userId,Integer number){
        SysUser sysUser = SysUser.builder().id(userId).memberNumber(number).build();
        updateById(sysUser);
    }

    @Override public Integer getMemberNumber(Integer userId){
        String redisKey = USER_NUMBER_KEY + ":" + userId;
        return Optional.ofNullable(redisObjectTemplate.opsForValue().get(redisKey))
                .map(object -> Integer.valueOf(object.toString())).orElse(0);
    }

}
