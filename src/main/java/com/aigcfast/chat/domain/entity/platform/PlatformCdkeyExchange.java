package com.aigcfast.chat.domain.entity.platform;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.aigcfast.chat.common.enums.ExtendSourceEnum;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 用户延长记录表
 * @Author lcy
 * @Date 2023-07-12
 */
@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName("platform_cdkey_exchange")
public class PlatformCdkeyExchange implements Serializable {

    /** id */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /** 用户id */
    private Integer userId;

    /** 来源:invite,buy */
    private ExtendSourceEnum source;

    /** cdkey */
    private String cdkey;

    /** 次数 */
    private Integer number;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

}
