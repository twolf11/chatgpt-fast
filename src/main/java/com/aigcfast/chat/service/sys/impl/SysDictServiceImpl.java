
package com.aigcfast.chat.service.sys.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.aigcfast.chat.domain.entity.sys.SysDict;
import com.aigcfast.chat.mapper.sys.SysDictMapper;
import com.aigcfast.chat.service.sys.SysDictService;
import com.aigcfast.chat.util.Tools;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 字典表service实现
 * @Author lcy
 * @Date 2023-07-12
 */
@Service
@AllArgsConstructor
@Slf4j
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper,SysDict> implements SysDictService {

    @Override public List<SysDict> getByDictType(String dictType){
        LambdaQueryWrapper<SysDict> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysDict :: getDictType,dictType);
        return list(queryWrapper);
    }

    @Override public SysDict getOneByDictType(String dictType){
        List<SysDict> sysDicts = getByDictType(dictType);
        return Tools.isNotEmpty(sysDicts) ? sysDicts.get(0) : null;
    }

    @Override public void delete(String dictType,String dictCode){
        LambdaUpdateWrapper<SysDict> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SysDict :: getDictType,dictType).eq(SysDict :: getDictCode,dictCode);
        remove(updateWrapper);
    }

}
