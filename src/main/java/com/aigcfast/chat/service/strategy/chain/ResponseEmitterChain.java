package com.aigcfast.chat.service.strategy.chain;

import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import com.aigcfast.chat.domain.request.chat.ChatMessageRequest;

/**
 * 链路接口
 * @Author lcy
 * @Date 2023/7/26 10:51
 */
public interface ResponseEmitterChain {

    /**
     * 处理请求
     * @param request 请求对象
     * @param emitter 响应对象
     */
    void doChain(ChatMessageRequest request,ResponseBodyEmitter emitter);

    /**
     * 处理下一个处理器
     * @param request 请求对象
     * @param emitter 响应对象
     */
    void next(ChatMessageRequest request,ResponseBodyEmitter emitter);

    /**
     * 设置下一个处理器
     * @param next 下一个处理器
     */
    void setNext(ResponseEmitterChain next);

    /**
     * 获取下一个处理器
     * @return 下一个处理器
     */
    ResponseEmitterChain getNext();

    /**
     * 获取前一个处理器
     * @return 前一个处理器
     */
    ResponseEmitterChain getPrev();

    /**
     * 设置前一个处理器
     * @param prev 前一个处理器
     */
    void setPrev(ResponseEmitterChain prev);

}
