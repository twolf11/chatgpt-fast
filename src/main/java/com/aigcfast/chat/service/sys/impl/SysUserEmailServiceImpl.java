
package com.aigcfast.chat.service.sys.impl;

import org.springframework.stereotype.Service;

import com.aigcfast.chat.util.Tools;
import com.aigcfast.chat.domain.entity.sys.SysUserEmail;
import com.aigcfast.chat.mapper.sys.SysUserEmailMapper;
import com.aigcfast.chat.service.common.impl.CacheServiceImpl;
import com.aigcfast.chat.service.sys.SysUserEmailService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.aigcfast.chat.common.constant.RedisConstant.USER_EMAIL_KEY;

/**
 * 前端用户邮箱登录service实现
 * @Author lcy
 * @Date 2023-07-12
 */
@Service
@AllArgsConstructor
@Slf4j
public class SysUserEmailServiceImpl extends CacheServiceImpl<SysUserEmailMapper,SysUserEmail> implements SysUserEmailService {

    @Override public Object getKey(SysUserEmail value){
        return value.getEmail();
    }

    @Override public String getCacheName(){
        return USER_EMAIL_KEY;
    }

    @Override public SysUserEmail getByEmail(String email){
        SysUserEmail sysUserEmail = getCache(email);
        if (Tools.isEmpty(sysUserEmail)) {
            sysUserEmail = this.getOne(new LambdaQueryWrapper<SysUserEmail>()
                    .eq(SysUserEmail :: getEmail,email),false);
            addCache(sysUserEmail);
        }
        return sysUserEmail;
    }

    @Override public boolean save(SysUserEmail entity){
        super.save(entity);
        addCache(entity);
        return true;
    }

    @Override public boolean updateById(SysUserEmail entity){
        boolean b = super.updateById(entity);
        SysUserEmail byId = super.getById(entity.getId());
        addCache(byId);
        return b;
    }

    @Override public void deleteEntity(String email){
        deleteCache(email);
        LambdaQueryWrapper<SysUserEmail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserEmail :: getEmail,email);
        remove(queryWrapper);
    }
}
