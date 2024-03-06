package com.aigcfast.chat.domain.convert;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.aigcfast.chat.domain.entity.sys.SysUser;
import com.aigcfast.chat.domain.vo.sys.UserInfoVO;

/**
 * 用户转换接口
 * @Author lcy
 * @Date 2023/7/12 18:22
 */
@Mapper
public interface UserConvert {

    UserConvert INSTANCE = Mappers.getMapper(UserConvert.class);

    /**
     * entityToVo
     * @param sysUser entity
     * @return Vo
     * @author lcy
     * @date 2023/7/12 18:23
     **/
    UserInfoVO.UserVO entityToVo(SysUser sysUser);

}
