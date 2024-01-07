package com.zch.label.domain.po;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * tag
 * @author Poison02
 */
@Data
public class Tag implements Serializable {
    /**
     * 主键;标签id
     */
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
