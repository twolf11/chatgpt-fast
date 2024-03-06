package com.aigcfast.chat.service.sys;

import com.aigcfast.chat.domain.entity.sys.SysUserInvite;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 用户邀请记录表service
 * @Author lcy
 * @Date 2023-07-12
 */
public interface SysUserInviteService extends IService<SysUserInvite> {

    /**
     * 邀请用户
     * @param sysUserInvite 用户信息
     * @author lcy
     * @date 2023/5/24 17:26
     **/
    boolean inviteUser(SysUserInvite sysUserInvite);
}
