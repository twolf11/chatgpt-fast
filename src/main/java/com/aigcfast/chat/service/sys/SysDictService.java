package com.aigcfast.chat.service.sys;

import java.util.List;

import com.aigcfast.chat.domain.entity.sys.SysDict;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 字典表service
 * @Author lcy
 * @Date 2023-07-12
 */
public interface SysDictService extends IService<SysDict> {

    /**
     * 根据字典类型获取字典列表
     * @param dictType 字典类型
     * @author lcy
     * @date 2023/6/7 14:50
     **/
    List<SysDict> getByDictType(String dictType);

    /**
     * 根据字典类型获取字典,取第一个数据
     * @param dictType 字典类型
     * @author lcy
     * @date 2023/6/7 14:50
     **/
    SysDict getOneByDictType(String dictType);

    /**
     * 删除字典
     * @param dictType 字典类型
     * @param dictCode 字典编码
     * @author lcy
     * @date 2023/7/14 17:10
     **/
    void delete(String dictType,String dictCode);
}
