package com.zch.label.domain.query;

import com.zch.common.domain.entity.BasePageQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Poison02
 * @date 2024/1/7
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryTagQuery extends BasePageQuery {

    /**
     * 分类 id
     */
    private Long categoryId;

    /**
     * 分类名
     */
    private String categoryName;

    /**
     * 分类类型
     */
    private String type;

    /**
     * 标签id
     */
    private Long tagId;

    /**
     * 标签名
     */
    private String tagName;

}
