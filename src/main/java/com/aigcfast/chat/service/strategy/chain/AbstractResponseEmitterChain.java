package com.aigcfast.chat.service.strategy.chain;

import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import com.aigcfast.chat.domain.request.chat.ChatMessageRequest;

/**
 * 链路抽象类
 * @Author lcy
 * @Date 2023/7/26 10:58
 */
public abstract class AbstractResponseEmitterChain implements ResponseEmitterChain {

    /**
     * 下一个节点
     */
    private ResponseEmitterChain next;

    /**
     * 上一个节点
     */
    private ResponseEmitterChain prev;

    @Override public void next(ChatMessageRequest request,ResponseBodyEmitter emitter){
        if (getNext() != null) {
            getNext().doChain(request,emitter);
        }
    }

    @Override
    public void setNext(ResponseEmitterChain next){
        this.next = next;
        next.setPrev(this);
    }

    @Override
    public ResponseEmitterChain getNext(){
        return next;
    }

    @Override
    public ResponseEmitterChain getPrev(){
        return prev;
    }

    @Override
    public void setPrev(ResponseEmitterChain prev){
        this.prev = prev;
    }
}
