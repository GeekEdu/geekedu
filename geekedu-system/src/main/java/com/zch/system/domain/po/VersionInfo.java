package com.zch.system.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zch.common.domain.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Poison02
 * @date 2024/1/15
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("info")
public class VersionInfo extends BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String springbootVersion;

    private String geekeduVersion;

    private String jdkVersion;

    private Boolean isDelete;

}
