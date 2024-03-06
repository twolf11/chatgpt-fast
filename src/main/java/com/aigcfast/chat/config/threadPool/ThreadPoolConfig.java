package com.aigcfast.chat.config.threadPool;

import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import lombok.Setter;

/**
 * 线程池配置
 * @Author lcy
 * @Date 2023/5/31 15:08
 */
@Setter
@Configuration
@EnableAsync
@ConfigurationProperties(prefix = "task.pool")
public class ThreadPoolConfig {

    /** 线程池中的核心线程数量,默认为2 */
    private int corePoolSize = 2;

    /** 线程池中的最大线程数量 */
    private int maxPoolSize = 2;

    /** 线程池中允许线程的空闲时间,默认为 60s */
    private int keepAliveTime = 30;

    /** 线程池中的队列最大数量 */
    private int queueCapacity = 2000;

    /** 线程的名称前缀 */
    private static final String THREAD_PREFIX = "async-db-runner-%d";

    @Bean
    @Lazy
    public ThreadPoolTaskExecutor threadPool(){

        ThreadPoolTaskExecutor executor = new CustomizeThreadPoolExecutor();

        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setKeepAliveSeconds(keepAliveTime);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix(THREAD_PREFIX);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());

        executor.initialize();

        return executor;
    }
}
