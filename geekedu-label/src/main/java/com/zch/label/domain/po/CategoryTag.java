package com.zch.label.domain.po;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * category_tag
 * @author Poison02
 */
@Data
public class CategoryTag implements Serializable {
    /**
     * 主键
     */
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
     * 类型;属于哪种类型的分类，1-课程，2-图文，3-问答
     */
    private Short type;

    /**
     * 创建人
     */
    private Long createdBy;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 更新人
     */
    private Long updatedBy;

    /**
     * 更新时间
     */
    private Date updatedTime;

    /**
     * 是否删除;0-未删除，1-删除
     */
    private Short isDelete;

    private static final long serialVersionUID = 1L;

}
