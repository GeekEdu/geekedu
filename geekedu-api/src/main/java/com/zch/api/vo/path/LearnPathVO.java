package com.zch.api.vo.path;

import com.zch.api.vo.label.CategorySimpleVO;
import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Poison02
 * @date 2024/3/14
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LearnPathVO extends BaseVO {

    private Integer id;

    /**
     * 分类id
     */
    private Integer categoryId;

    /**
     * 路径名
     */
    private String name;

    /**
     * 现价
     */
    private BigDecimal price;

    /**
     * 封面
     */
    private String cover;

    /**
     * 原价 所有商品的总价
     */
    private BigDecimal originPrice;

    /**
     * 是否展示
     */
    private Boolean isShow;

    /**
     * 上架时间
     */
    private LocalDateTime groundingTime;

    /**
     * 步骤数
     */
    private Integer stepCount;

    /**
     * 课程数
     */
    private Integer courseCount;

    /**
     * 简介
     */
    private String intro;

    private LocalDateTime createdTime;

    private CategorySimpleVO category;

}
