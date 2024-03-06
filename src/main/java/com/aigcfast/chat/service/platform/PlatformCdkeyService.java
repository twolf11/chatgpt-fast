package com.aigcfast.chat.service.platform;

import com.aigcfast.chat.domain.entity.platform.PlatformCdkey;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 平台cdkey service
 * @Author lcy
 * @Date 2023-07-12
 */
public interface PlatformCdkeyService extends IService<PlatformCdkey> {

    /**
     * 生成指定次数的cdkey
     * @param number 次数
     * @return String
     * @author lcy
     * @date 2023/8/2 10:39
     **/
    String generateCdkey(Integer number);

    /**
     * 兑换cdkey
     * @param cdkey cdkey
     * @return boolean
     * @author lcy
     * @date 2023/8/2 10:55
     **/
    boolean exchangeCdkey(String cdkey);

    /**
     * 根据cdkey获取信息
     * @param cdkey cdkey
     * @return com.aigcfast.chat.domain.entity.platform.PlatformCdkey
     * @author lcy
     * @date 2023/8/2 10:51
     **/
    PlatformCdkey getByCdkey(String cdkey);

    /**
     * 邀请延长次数
     * @param userId 基础用户 id
     * @param number 天数
     * @author lcy
     * @date 2023/5/24 17:26
     **/
    boolean inviteExtendNumber(Integer userId,Integer number);

}
