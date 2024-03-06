package com.aigcfast.chat.common.response;

/**
 * @Description 返回值接口
 * @Author lcy
 * @Date 2019/12/26 16:21
 */
public interface ResultCode {

    /**
     * 操作代码
     * @return java.lang.Integer
     * @author lcy
     * @date 2019/12/26 16:22
     **/
    Integer getCode();

    /**
     * 提示信息
     * @return java.lang.String
     * @author lcy
     * @date 2019/12/26 16:22
     **/
    String getMessage();

}