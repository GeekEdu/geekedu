package com.zch.label.domain.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zch.common.mvc.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * category
 * @author Poison02
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("category")
public class Category extends BaseEntity {
    /**
     * 主键;分类id
     */
    @TableId("id")
    private Long id;

    /**
     * 分类名
     */
    private String name;

    /**
     * 类型;属于哪种类型的分类，1-录播课，2-直播课，3-图文，4-电子书，5-学习路线
     */
    private Short type;

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
