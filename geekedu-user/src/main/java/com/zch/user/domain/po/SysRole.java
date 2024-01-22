package com.zch.user.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zch.common.mvc.entity.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Poison02
 * @date 2024/1/12
 */
@Data
@TableName("sys_role")
public class SysRole extends BaseEntity implements Serializable {

    private static final Long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String displayName;

    private String slug;

    private String description;

    private Long createdBy;

    private Long updatedBy;

    private Boolean isDelete;

}
