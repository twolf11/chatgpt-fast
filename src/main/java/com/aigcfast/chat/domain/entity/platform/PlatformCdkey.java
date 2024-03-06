package com.aigcfast.chat.domain.entity.platform;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.aigcfast.chat.common.enums.CdkeyUseEnum;
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
 * 用户会员套餐
 * @Author lcy
 * @Date 2023-07-12
 */
@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName("platform_cdkey")
public class PlatformCdkey implements Serializable {

    /** id */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /** cdkey */
    private String cdkey;

    /** 次数 */
    private Integer number;

    /** 是否使用 */
    private CdkeyUseEnum isUse;

    /** ip */
    private String ip;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

}
