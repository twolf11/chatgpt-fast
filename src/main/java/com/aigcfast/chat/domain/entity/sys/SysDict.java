package com.aigcfast.chat.domain.entity.sys;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 字典表
 * @Author lcy
 * @Date 2023-07-12
 */
@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_dict")
public class SysDict extends Model<SysDict> {

    /** id */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 字典类型 */
    private String dictType;

    /** 字典编码 */
    private String dictCode;

    /** 字典值 */
    private String dictValue;

    /** 描述 */
    private String description;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
