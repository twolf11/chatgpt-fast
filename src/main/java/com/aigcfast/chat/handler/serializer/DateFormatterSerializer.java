package com.aigcfast.chat.handler.serializer;

import java.io.IOException;
import java.util.Date;

import cn.hutool.core.date.DateUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * 日期序列化
 * @author lcy
 * @date 2023-6-25
 */
public class DateFormatterSerializer extends JsonSerializer<Date> {

    @Override
    public void serialize(Date date,JsonGenerator jsonGenerator,SerializerProvider serializerProvider) throws IOException{
        jsonGenerator.writeString(DateUtil.formatDateTime(date));
    }
}
