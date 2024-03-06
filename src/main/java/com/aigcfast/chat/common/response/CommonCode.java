package com.aigcfast.chat.common.response;

import jakarta.servlet.http.HttpServletResponse;

/**
 * @Description 公共错误返回值
 * @Author lcy
 * @Date 2019/12/26 20:16
 */
public enum CommonCode implements ResultCode {
    /**
     * 操作成功
     */
    SUCCESS(200,"操作成功！"),
    /**
     * 请求未授权
     */
    UN_AUTHORIZED(HttpServletResponse.SC_UNAUTHORIZED,"请求未授权"),
    /**
     * 系统异常
     */
    SERVER_EXCEPTION(500,"抱歉，系统出现异常，请联系系统管理员！"),
    /**
     * 操作失败
     */
    FAIL(500,"操作失败！"),
    /**
     * 非法参数
     */
    INVALID_PARAM(602,"非法参数！"),
    /**
     * 必要参数为空
     */
    PARAMETER_NULL(603,"必要参数不能为空"),
    /**
     * 没有登录
     */
    UN_LOGIN(604,"此操作需要登陆系统！"),

    /**
     * 增加失败
     */
    ADD_FAIL(606,"新增失败"),
    /**
     * 删除失败
     */
    DELETE_FAIL(607,"删除失败"),
    /**
     * 修改失败
     */
    UPDATE_FAIL(608,"修改失败"),
    /**
     * 该记录已存在
     */
    RECORD_EXIT(609,"该记录关键字段已经存在"),
    /**
     * 无查询结果
     */
    NO_QUERY_RESULT(611,"无查询结果!"),
    /**
     * 找不到路径
     */
    PATH_NOT_FOUND(612,"未找到路径");

    /**
     * 操作代码
     */
    private final Integer code;

    /**
     * 提示信息
     */
    private final String message;

    CommonCode(Integer code,String message){
        this.code = code;
        this.message = message;
    }

    @Override public Integer getCode(){
        return code;
    }

    @Override public String getMessage(){
        return message;
    }
}