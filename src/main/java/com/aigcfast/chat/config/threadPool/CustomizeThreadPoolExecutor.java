package com.aigcfast.chat.config.threadPool;

import java.util.concurrent.Future;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import lombok.extern.slf4j.Slf4j;

/**
 * 自定义线程池
 * @Author lcy
 * @Date 2023/6/7 17:48
 */
@Slf4j
public class CustomizeThreadPoolExecutor extends ThreadPoolTaskExecutor {

    @Override
    public void execute(Runnable command){
        super.execute(wrap(command,Thread.currentThread().getName()));
    }

    @Override
    public Future<?> submit(Runnable task){
        return super.submit(wrap(task,Thread.currentThread().getName()));
    }

    private Runnable wrap(final Runnable task,String clientThreadName){
        return () -> {
            try {
                task.run();
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        };
    }

}
