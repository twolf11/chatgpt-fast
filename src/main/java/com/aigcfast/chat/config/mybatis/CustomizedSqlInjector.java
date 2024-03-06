package com.aigcfast.chat.config.mybatis;

import java.util.List;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;

/**
 * 自定义方法SQL注入器
 * @Author lcy
 * @Date 2022/5/23 18:09
 */
public class CustomizedSqlInjector extends DefaultSqlInjector {

    /**
     * 如果只需增加方法，保留mybatis plus自带方法， 可以先获取super.getMethodList()，再添加add
     * @param mapperClass mapper的class
     * @param tableInfo   表对象
     * @return java.util.List<com.baomidou.mybatisplus.core.injector.AbstractMethod>
     * @author lcy
     * @date 2022/5/23 18:20
     **/
    @Override public List<AbstractMethod> getMethodList(Class<?> mapperClass,TableInfo tableInfo){
        List<AbstractMethod> methodList = super.getMethodList(mapperClass,tableInfo);
        methodList.add(new InsertBatchMethod("insertBatch"));
        methodList.add(new UpdateBatchMethod("updateBatch"));
        return methodList;
    }
}