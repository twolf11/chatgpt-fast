package com.aigcfast.chat.config.mybatis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusPropertiesCustomizer;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.handlers.MybatisEnumTypeHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;

/**
 * @Description mybatis-plus配置
 * @Author lcy
 * @Date 2019/10/24 19:12
 */
@Configuration
public class MybatisPlusConfiguration {

    /**
     * 插件--3.4以后通过插件管理类夹在
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        //乐观锁
        mybatisPlusInterceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        //分页插件
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return mybatisPlusInterceptor;
    }

    /**
     * mybatisplus全局配置类
     * @return com.baomidou.mybatisplus.autoconfigure.MybatisPlusPropertiesCustomizer
     * @author lcy
     * @date 2022/2/21 17:04
     **/
    @Bean
    public MybatisPlusPropertiesCustomizer mybatisPlusPropertiesCustomizer(){
        return properties -> {
            GlobalConfig globalConfig = properties.getGlobalConfig();
            globalConfig.setBanner(false);
            MybatisConfiguration configuration = new MybatisConfiguration();
            //枚举映射
            configuration.setDefaultEnumTypeHandler(MybatisEnumTypeHandler.class);
            properties.setConfiguration(configuration);
        };
    }

    /**
     * 自定义sql注入器
     * @return com.lcy.common.orm.handler.CustomizedSqlInjector
     * @author lcy
     * @date 2022/5/24 18:02
     **/
    @Bean
    public CustomizedSqlInjector customizedSqlInjector(){
        return new CustomizedSqlInjector();
    }
}
