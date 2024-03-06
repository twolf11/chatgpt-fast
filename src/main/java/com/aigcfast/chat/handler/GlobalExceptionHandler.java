package com.aigcfast.chat.handler;

import cn.hutool.core.bean.BeanDesc;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.aigcfast.chat.service.blackList.BlackListService;
import com.aigcfast.chat.util.WebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import cn.dev33.satoken.exception.NotLoginException;
import com.aigcfast.chat.common.exception.ServiceException;
import com.aigcfast.chat.common.response.CommonCode;
import com.aigcfast.chat.common.response.Result;
import jakarta.servlet.http.HttpServletRequest;

import static cn.dev33.satoken.exception.NotLoginException.*;

/**
 * @Description 异常捕获
 * @Author lcy
 * @Date 2020/12/7 14:18
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);



    @ExceptionHandler(NotLoginException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result<Void> handleError(NotLoginException e){
        String type = e.getType();
        // 判断场景值，定制化异常信息
        String message;
        if (NOT_TOKEN.equals(type)) {
            message = DEFAULT_MESSAGE;
        } else if (TOKEN_TIMEOUT.equals(type) || INVALID_TOKEN.equals(type)) {
            message = "登录已失效，请重新登陆";
        } else if (BE_REPLACED.equals(type)) {
            message = "账号已在其它地方登陆";
        } else if (KICK_OUT.equals(type)) {
            message = "您已被强制下线";
        } else {
            message = DEFAULT_MESSAGE;
        }
        log.debug("鉴权拦截,提示:{}",message);
        return Result.create(CommonCode.UN_AUTHORIZED.getCode(),message);
    }

    /**
     * Exception异常
     * @param request   请求对象
     * @param exception 异常信息
     * @author lcy
     * @date 2019/10/26 9:44
     **/
    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Object> exception(HttpServletRequest request,Exception exception){
        log.error("system error! requestURI:{} ",request.getRequestURI(),exception);
        return Result.error();
    }

    /**
     * 自定义服务异常
     * @param request          请求对象
     * @param serviceException 异常信息
     * @author lcy
     * @date 2019/10/26 9:44
     **/
    @ExceptionHandler({ServiceException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<String> serviceException(HttpServletRequest request,ServiceException serviceException){
        log.error("ServiceException requestURI:{},message:{} ",request.getRequestURI(),serviceException.getMessage());
        return Result.create(serviceException.getCode(),serviceException.getMessage());
    }



    /**
     * 参数校验异常
     * @param request       请求对象
     * @param bindException 异常信息
     * @author lcy
     * @date 2019/10/26 9:44
     **/
    @ExceptionHandler({BindException.class,MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Object> bindException(HttpServletRequest request,BindException bindException){
        String ip = WebUtil.getIp();
        BlackListService blackListService = SpringUtil.getBean(BlackListService.class);
        log.error("请求参数为空!拉黑ip:{}",ip);
        //blackListService.addBlackIp(ip);
        StringBuilder stringBuilder = new StringBuilder();
        //获取所有的错误参数提示
        bindException.getBindingResult().getFieldErrors()
                .forEach(fieldError -> stringBuilder.append(fieldError.getDefaultMessage()).append(","));

        //如果有错误提示，去除最后的逗号。
        if (stringBuilder.length() > 0) {
            stringBuilder.setLength(stringBuilder.length() - 1);
        } else {
            stringBuilder.append("传入参数异常！");
        }

        return Result.error(stringBuilder.toString());
    }

}
