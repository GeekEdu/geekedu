package com.zch.label.domain.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zch.common.mvc.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * category_tag
 * @author Poison02
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("category_tag")
public class CategoryTag extends BaseEntity {
    /**
     * 主键
     */
    @TableId("id")
    private Long id;

    /**
     * 分类id
     */
    private Long categoryId;

    /**
     * 标签id
     */
    private Long tagId;

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
     * 是否删除;0-未删除，1-删除
     */
    private Short isDelete;

}
