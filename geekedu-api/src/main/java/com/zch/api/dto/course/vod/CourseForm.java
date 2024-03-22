package com.zch.api.dto.course.vod;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Poison02
 * @date 2024/3/22
 */
@Data
public class CourseForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 课程标题
     */
    private String title;

    /**
     * 课程售卖类型 1-免费 0-收费
     */
    private Boolean isFree;

    /**
     * 是否vip免费
     */
    private Boolean isVipFree;

    /**
     * 课程价格
     */
    private BigDecimal price;

    /**
     * 课程描述
     */
    private String description;

    /**
     * 详细介绍
     */
    private String intro;

    /**
     * 课程封面
     */
    private String coverLink;

    /**
     * 上架时间
     */
    private LocalDateTime groundingTime;

    /**
     * 是否显示
     */
    private Boolean isShow;

    /**
     * 分类id
     */
    private Integer categoryId;

}
