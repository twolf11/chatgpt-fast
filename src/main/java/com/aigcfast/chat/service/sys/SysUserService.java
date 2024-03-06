package com.aigcfast.chat.service.sys;

import com.aigcfast.chat.domain.entity.sys.SysUser;
import com.aigcfast.chat.service.common.CacheService;

/**
 * 前端用户基础信息表service
 * @Author lcy
 * @Date 2023-07-12
 */
public interface SysUserService extends CacheService<SysUser> {

    /**
     * 创建用户
     * @return com.aigcfast.chat.web.domain.entity.sys.SysUser
     * @author lcy
     * @date 2023/7/12 14:40
     **/
    SysUser createUser();

    /**
     * 更新上次登录 ip
     * @param userId 基础用户 id
     * @param ip         ip
     */
    void updateLastLoginIp(Integer userId,String ip);

    /**
     * 延长使用次数
     * @param userId 用户id
     * @param number     延长的次数
     * @author lcy
     * @date 2023/6/9 16:28
     **/
    void extendMemberNumber(Integer userId,Integer number);

    /**
     * 获取使用次数
     * @param userId 用户id
     * @author lcy
     * @date 2023/6/9 16:28
     **/
    Integer getMemberNumber(Integer userId);

}
