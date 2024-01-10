package com.zch.label.domain.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Poison02
 * @date 2024/1/7
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryTagQuery {

    private Long categoryId;

    private String categoryName;

    private String type;

    private Long tagId;

    private String tagName;

    private Integer pageNum = 1;

    private Integer pageSize = 10;

}
