package com.zch.oss.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zch.common.mvc.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Poison02
 * @date 2024/3/25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("attach")
public class Attach extends BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    private Integer courseId;

    private String path;

    private String extension;

    private Long size;

    @TableLogic
    private Boolean isDelete;

}
