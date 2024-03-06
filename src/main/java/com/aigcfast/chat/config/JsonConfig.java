package com.aigcfast.chat.config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.aigcfast.chat.handler.serializer.DateFormatterSerializer;
import com.aigcfast.chat.handler.serializer.LongToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

/**
 * json配置类
 * @Author lcy
 * @Date 2022/5/6 16:20
 */
@Configuration
public class JsonConfig {

    /** 序列化参数，如果配置为空则用默认 */
    @Value("${spring.jackson.date-format:yyyy-MM-dd HH:mm:ss}")
    private String pattern;

    @Bean
    public LocalDateTimeSerializer localDateTimeSerializer(){
        return new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(pattern));
    }

    @Bean
    public LocalDateTimeDeserializer localDateTimeDeserializer(){
        return new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(pattern));
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer(){
        return builder -> builder.serializerByType(LocalDateTime.class,localDateTimeSerializer()).deserializerByType(LocalDateTime.class,localDateTimeDeserializer())
                .serializerByType(Date.class,new DateFormatterSerializer())
                .serializerByType(Long.class,new LongToStringSerializer())
                .serializerByType(Date.class,new DateFormatterSerializer());
    }
}
