package com.zch.label.domain.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zch.common.mvc.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * tag
 * @author Poison02
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("tag")
public class Tag extends BaseEntity {
    /**
     * 主键;标签id
     */
    @TableId("id")
    private Long id;

    /**
     * 标签名
     */
    private String name;

    /**
     * 创建人
     */
    private Long createdBy;

    /**
     * 更新人
     */
    private Long updatedBy;

    /**
     * 是否删除
     */
    private Short isDelete;

}
