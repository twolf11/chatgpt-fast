package com.aigcfast.chat.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

/**
 * @Description json工具类--jackson为主
 * @Author lcy
 * @Date 2021/11/16 11:32
 */
public class JsonUtil {

    private static final Logger log = LoggerFactory.getLogger(JsonUtil.class);

    /**
     * json操作对象--转成json的时候是下划线
     */
    private static final ObjectMapper OBJECT_MAPPER_LINE = new ObjectMapper();

    /**
     * json操作对象--默认转成json的时候不是下划线
     */
    private static final ObjectMapper OBJECT_MAPPER_NO_LINE = new ObjectMapper();

    static{
        //创建jsr310包下的javaTimeModule对象，其中包含了jdk8以后的时间类型序列化和反序列化配置
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        //LocalDateTime序列化设置为yyyy-MM-dd HH:mm:ss
        LocalDateTimeSerializer localDateTimeSerializer = new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateSerializer localDateSerializer = new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalTimeSerializer localTimeSerializer = new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss"));
        //增加序列化到配置当中
        javaTimeModule.addSerializer(LocalDateTime.class,localDateTimeSerializer);
        javaTimeModule.addSerializer(LocalDate.class,localDateSerializer);
        javaTimeModule.addSerializer(LocalTime.class,localTimeSerializer);
        //LocalDateTime反序列化设置为yyyy-MM-dd HH:mm:ss
        LocalDateTimeDeserializer localDateTimeDeserializer = new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateDeserializer localDateDeserializer = new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalTimeDeserializer localTimeDeserializer = new LocalTimeDeserializer(DateTimeFormatter.ofPattern("HH:mm:ss"));
        //增加反序列化到配置当中
        javaTimeModule.addDeserializer(LocalDateTime.class,localDateTimeDeserializer);
        javaTimeModule.addDeserializer(LocalDate.class,localDateDeserializer);
        javaTimeModule.addDeserializer(LocalTime.class,localTimeDeserializer);

        //设置驼峰转成下划线
        OBJECT_MAPPER_LINE.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        // 遇到未知属性是否抛出异常 ，默认是抛出异常的
        OBJECT_MAPPER_LINE.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        // 当实体类没有setter方法时，序列化不报错，返回一个空对象
        OBJECT_MAPPER_LINE.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
        // json不进行换行缩进等操作  默认就是不进行操作
        OBJECT_MAPPER_LINE.disable(SerializationFeature.INDENT_OUTPUT);
        //属性为null的不进行转换
        OBJECT_MAPPER_LINE.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        OBJECT_MAPPER_LINE.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        OBJECT_MAPPER_LINE.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        //注册配置--jdk8类型
        OBJECT_MAPPER_LINE.registerModule(javaTimeModule);

        // 遇到未知属性是否抛出异常 ，默认是抛出异常的
        OBJECT_MAPPER_NO_LINE.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        // 当实体类没有setter方法时，序列化不报错，返回一个空对象
        OBJECT_MAPPER_NO_LINE.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
        // json不进行换行缩进等操作  默认就是不进行操作
        OBJECT_MAPPER_NO_LINE.disable(SerializationFeature.INDENT_OUTPUT);
        //属性为null的不进行转换
        OBJECT_MAPPER_NO_LINE.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        OBJECT_MAPPER_NO_LINE.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        OBJECT_MAPPER_NO_LINE.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        //注册配置--jdk8类型
        OBJECT_MAPPER_NO_LINE.registerModule(javaTimeModule);
    }

    /**
     * 增加接口方法可以使用自定义函数--转下划线的objectMapper
     * @param function 函数实现
     * @return R
     * @author lcy
     * @date 2022/5/6 11:50
     **/
    public static <R> R doJsonLine(Function<ObjectMapper,R> function){
        return function.apply(OBJECT_MAPPER_LINE);
    }

    /**
     * 增加接口方法可以使用自定义函数--不转下划线的objectMapper
     * @param function 函数实现
     * @return R
     * @author lcy
     * @date 2022/5/6 11:50
     **/
    public static <R> R doJson(Function<ObjectMapper,R> function){
        return function.apply(OBJECT_MAPPER_NO_LINE);
    }

    /**
     * 对象转成Object节点
     * @param object 转换的对象
     * @return com.fasterxml.jackson.databind.node.ObjectNode
     * @author lcy
     * @date 2022/4/28 14:11
     **/
    public static ObjectNode objectToJsonNodeLine(Object object){
        return OBJECT_MAPPER_LINE.valueToTree(object);
    }

    /**
     * json转成Object节点
     * @param json 转换的对象
     * @return com.fasterxml.jackson.databind.node.ObjectNode
     * @author lcy
     * @date 2022/4/28 14:11
     **/
    public static ObjectNode jsonToJsonNodeLine(String json){
        try {
            return (ObjectNode)OBJECT_MAPPER_LINE.readTree(json);
        } catch (JsonProcessingException e) {
            log.error("json to object error. ",e);
        }
        return null;
    }

    /**
     * json转成Object节点
     * @param json 转换的对象
     * @return com.fasterxml.jackson.databind.node.ObjectNode
     * @author lcy
     * @date 2022/4/28 14:11
     **/
    public static ObjectNode jsonToJsonNode(String json){
        try {
            return (ObjectNode)OBJECT_MAPPER_NO_LINE.readTree(json);
        } catch (JsonProcessingException e) {
            log.error("json to object error. ",e);
        }
        return null;
    }

    /**
     * 对象转成Object节点
     * @param object 转换的对象
     * @return com.fasterxml.jackson.databind.node.ObjectNode
     * @author lcy
     * @date 2022/4/28 14:11
     **/
    public static ObjectNode objectToJsonNode(Object object){
        return OBJECT_MAPPER_NO_LINE.valueToTree(object);
    }

    /**
     * 对象转json
     * @param object 对象
     * @return java.lang.String
     * @author lcy
     * @date 2021/11/16 11:36
     **/
    public static String objectToJsonLine(Object object){
        String json = "";
        try {
            json = OBJECT_MAPPER_LINE.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("object to json error. ",e);
        }
        return json;
    }

    /**
     * json转对象
     * @param json  json字符串
     * @param clazz 需要转换的类对象
     * @return java.lang.String
     * @author lcy
     * @date 2021/11/16 11:36
     **/
    public static <T> T jsonToObjectLine(String json,Class<T> clazz){
        T object = null;
        try {
            object = OBJECT_MAPPER_LINE.readValue(json,clazz);
        } catch (JsonProcessingException e) {
            log.error("json to object error. ",e);
        }
        return object;
    }

    /**
     * json转泛型对象
     * @param json         json字符串
     * @param valueTypeRef 泛型对象
     * @return java.lang.String
     * @author lcy
     * @date 2021/11/16 11:36
     **/
    public static <T> T jsonToObjectLine(String json,TypeReference<T> valueTypeRef){
        T object = null;
        try {
            object = OBJECT_MAPPER_LINE.readValue(json,valueTypeRef);
        } catch (JsonProcessingException e) {
            log.error("json to object error. ",e);
        }
        return object;
    }

    /**
     * 对象转json
     * @param object 对象
     * @return java.lang.String
     * @author lcy
     * @date 2021/11/16 11:36
     **/
    public static String objectToJson(Object object){
        String json = "";
        try {
            json = OBJECT_MAPPER_NO_LINE.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("object to json error. ",e);
        }
        return json;
    }

    /**
     * json转对象
     * @param json  json字符串
     * @param clazz 需要转换的类对象
     * @return java.lang.String
     * @author lcy
     * @date 2021/11/16 11:36
     **/
    public static <T> T jsonToObject(String json,Class<T> clazz){
        T object = null;
        try {
            object = OBJECT_MAPPER_NO_LINE.readValue(json,clazz);
        } catch (JsonProcessingException e) {
            log.error("json to object error. ",e);
        }
        return object;
    }

    /**
     * json转泛型对象
     * @param json         json字符串
     * @param valueTypeRef 泛型对象
     * @return java.lang.String
     * @author lcy
     * @date 2021/11/16 11:36
     **/
    public static <T> T jsonToObject(String json,TypeReference<T> valueTypeRef){
        T object = null;
        try {
            object = OBJECT_MAPPER_LINE.readValue(json,valueTypeRef);
        } catch (JsonProcessingException e) {
            log.error("json to object error. ",e);
        }
        return object;
    }
}
