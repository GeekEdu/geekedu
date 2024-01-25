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
    private Integer categoryId;

    /**
     * 分类名
     */
    private String name;

    /**
     * 分类类型，传对应的类型的文本即可 1-录播课，2-直播课，3-图文，4-电子书，5-学习路线
     */
    private String type;

}
