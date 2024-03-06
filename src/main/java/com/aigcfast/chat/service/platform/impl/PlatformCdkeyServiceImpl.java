
package com.aigcfast.chat.service.platform.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.aigcfast.chat.common.enums.CdkeyUseEnum;
import com.aigcfast.chat.common.enums.ExtendSourceEnum;
import com.aigcfast.chat.common.exception.ServiceException;
import com.aigcfast.chat.domain.entity.platform.PlatformCdkey;
import com.aigcfast.chat.domain.entity.platform.PlatformCdkeyExchange;
import com.aigcfast.chat.domain.entity.sys.SysUser;
import com.aigcfast.chat.mapper.platform.PlatformCdkeyMapper;
import com.aigcfast.chat.service.platform.PlatformCdkeyExchangeService;
import com.aigcfast.chat.service.platform.PlatformCdkeyService;
import com.aigcfast.chat.service.sys.SysUserService;
import com.aigcfast.chat.util.AssertUtil;
import com.aigcfast.chat.util.UserUtil;
import com.aigcfast.chat.util.WebUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 平台cdkey service实现
 * @Author lcy
 * @Date 2023-07-12
 */
@Service
@AllArgsConstructor
@Slf4j
public class PlatformCdkeyServiceImpl extends ServiceImpl<PlatformCdkeyMapper,PlatformCdkey> implements PlatformCdkeyService {

    private SysUserService sysUserService;

    private PlatformCdkeyExchangeService exchangeService;

    @Override public String generateCdkey(Integer number){
        String cdkey = UUID.randomUUID().toString().replace("-","");
        PlatformCdkey platformCdkey = PlatformCdkey.builder().cdkey(cdkey).number(number).isUse(CdkeyUseEnum.NO).ip(WebUtil.getIp()).build();
        if (save(platformCdkey)) {
            return cdkey;
        }
        throw new ServiceException("生成cdkey异常");
    }

    @Override public boolean exchangeCdkey(String cdkey){
        Integer userId = UserUtil.getUserId();
        SysUser sysUser = sysUserService.getById(userId);
        AssertUtil.isEmpty(sysUser,"用户不存在或已失效，请重新登录");
        PlatformCdkey platformCdkey = getByCdkey(cdkey);
        AssertUtil.isEmpty(platformCdkey,"cdkey无效");
        AssertUtil.isTrue(CdkeyUseEnum.YES.equals(platformCdkey.getIsUse()),"cdkey已兑换");
        PlatformCdkeyExchange platformCdkeyExchange = PlatformCdkeyExchange.builder()
                .cdkey(cdkey).userId(userId).source(ExtendSourceEnum.BUY).number(platformCdkey.getNumber())
                .build();
        exchangeService.save(platformCdkeyExchange);
        //更新状态
        platformCdkey.setIsUse(CdkeyUseEnum.YES);
        updateById(platformCdkey);

        sysUser.setMemberNumber(sysUser.getMemberNumber() + platformCdkey.getNumber());
        sysUserService.updateById(sysUser);

        return true;
    }

    @Override public PlatformCdkey getByCdkey(String cdkey){
        LambdaQueryWrapper<PlatformCdkey> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PlatformCdkey :: getCdkey,cdkey);
        return getOne(queryWrapper,false);
    }

    @Override public boolean inviteExtendNumber(Integer userId,Integer number){
        SysUser sysUser = sysUserService.getById(userId);
        AssertUtil.isEmpty(sysUser,"用户不存在或已失效，请重新登录");
        PlatformCdkeyExchange platformCdkeyExchange = PlatformCdkeyExchange.builder()
                .userId(userId).source(ExtendSourceEnum.INVITE).number(number)
                .build();
        exchangeService.save(platformCdkeyExchange);

        sysUser.setMemberNumber(sysUser.getMemberNumber() + number);
        return sysUserService.updateById(sysUser);
    }
}
