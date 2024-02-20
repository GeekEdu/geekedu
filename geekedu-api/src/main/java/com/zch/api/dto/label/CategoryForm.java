package com.zch.api.dto.label;

import lombok.Data;

/**
 * 一级分类就是分类，从二级分类开始就使用标签定义
 * @author Poison02
 * @date 2024/1/7
 */
@Data
public class CategoryForm {

    /**
     * 若选择了父级id，则传对应的分类id即可
     */
    private Integer parentId = 0;

    /**
     * 排序字段 越小展示越靠前
     */
    private Integer sort;

    /**
     * 分类名
     */
    private String name;

    /**
     * 分类类型
     * REPLAY_COURSE
     * LIVE_COURSE
     * IMAGE_TEXT
     * E_BOOK
     * LEARN_PATH
     * ASK_QUESTION
     */
    private String type;

}
