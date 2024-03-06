package com.aigcfast.chat.service.sys;

import com.aigcfast.chat.domain.entity.sys.SysUserEmail;
import com.aigcfast.chat.service.common.CacheService;

/**
 * 前端用户邮箱登录service
 * @Author lcy
 * @Date 2023-07-12
 */
public interface SysUserEmailService extends CacheService<SysUserEmail> {

    /**
     * 获取邮箱账号信息
     * @param email 邮箱
     * @author lcy
     * @date 2023/6/1 16:02
     **/
    SysUserEmail getByEmail(String email);

    /**
     * 删除
     * @param email 邮箱
     * @author lcy
     * @date 2023/6/1 17:04
     **/
    void deleteEntity(String email);

}
