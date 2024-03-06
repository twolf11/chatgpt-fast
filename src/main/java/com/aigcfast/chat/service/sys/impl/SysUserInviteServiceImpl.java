
package com.aigcfast.chat.service.sys.impl;

import org.springframework.stereotype.Service;

import com.aigcfast.chat.config.RegisterConfig;
import com.aigcfast.chat.domain.entity.sys.SysUserInvite;
import com.aigcfast.chat.mapper.sys.SysUserInviteMapper;
import com.aigcfast.chat.service.platform.PlatformCdkeyService;
import com.aigcfast.chat.service.sys.SysUserInviteService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 用户邀请记录表service实现
 * @Author lcy
 * @Date 2023-07-12
 */
@Service
@AllArgsConstructor
@Slf4j
public class SysUserInviteServiceImpl extends ServiceImpl<SysUserInviteMapper,SysUserInvite> implements SysUserInviteService {

    private PlatformCdkeyService platformCdkeyService;

    private RegisterConfig registerConfig;

    @Override public boolean inviteUser(SysUserInvite sysUserInvite){
        platformCdkeyService.inviteExtendNumber(sysUserInvite.getUserId(),registerConfig.getInviteNumber());
        return save(sysUserInvite);
    }
}
