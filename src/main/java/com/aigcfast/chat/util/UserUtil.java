package com.aigcfast.chat.util;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.json.JSONObject;
import com.aigcfast.chat.domain.vo.sys.UserInfoVO;
import lombok.experimental.UtilityClass;

import static com.aigcfast.chat.common.constant.UserConstant.JWT_USER_INFO;

/**
 * 用户工具类
 * @Author lcy
 * @Date 2023/7/12 10:46
 */
@UtilityClass
public class UserUtil {

    /**
     * 获取用户id
     * @return 用户 id
     */
    public Integer getUserId(){
        Object loginId = StpUtil.getLoginId();
        if (loginId == null) {
            return null;
        }
        return NumberUtil.parseInt(String.valueOf(loginId));
    }

    /**
     * 获取用户信息
     * @return 用户信息
     */
    public UserInfoVO getUserInfo(){
        //使用jwt获取到的变量则为JSONObject
        JSONObject object = (JSONObject)StpUtil.getExtra(JWT_USER_INFO);
        return object.toBean(UserInfoVO.class);
    }

}
