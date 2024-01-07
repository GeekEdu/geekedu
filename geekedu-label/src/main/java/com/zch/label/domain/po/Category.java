package com.zch.label.domain.po;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * category
 * @author Poison02
 */
@Data
public class Category implements Serializable {
    /**
     * 主键;分类id
     */
    private Long id;

    /**
     * 分类名
     */
    private String name;

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
     * 是否删除
     */
    private Short isDelete;

    private static final long serialVersionUID = 1L;

}
