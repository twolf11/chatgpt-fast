package com.aigcfast.chat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;

import cn.hutool.core.net.NetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 启动类
 * @Author lcy
 * @Date 2023/6/25 16:58
 */
@Slf4j
@EnableDiscoveryClient
@MapperScan("com.aigcfast.chat.mapper")
@SpringBootApplication
public class ChatgptWebApplication {

    public static void main(String[] args){
        // 启动开始时间
        long start = System.currentTimeMillis();
        ConfigurableApplicationContext context = SpringApplication.run(ChatgptWebApplication.class,args);
        long time = (System.currentTimeMillis() - start ) / 1000;
        String ipAddress = NetUtil.getLocalhostStr();
        String port = context.getEnvironment().getProperty("server.port");
        String info = "启动完成，耗时：%d秒， 接口文档地址：http://%s:%s/doc.html";
        log.info(String.format(info,time,ipAddress,port));
    }

}
