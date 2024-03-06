package com.aigcfast.chat.common.exception;

import com.aigcfast.chat.common.response.ResultCode;

/**
 * @Description 自定义服务异常
 * @Author lcy
 * @Date 2020/12/7 14:20
 */
public class ServiceException extends RuntimeException {

    /**
     * 响应码
     */
    private Integer code;

    public ServiceException(String message){
        super(message);
        this.code = 400;
    }

    public ServiceException(Integer code,String message){
        super(message);
        this.code = code;
    }

    public ServiceException(ResultCode resultCode){
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
    }

    public Integer getCode(){
        return code;
    }

    public ServiceException setCode(Integer code){
        this.code = code;
        return this;
    }
}
