package com.zch.course.domain.repository;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Poison02
 * @date 2024/3/22
 */
@Data
public class CourseInfoEs implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer courseId;

    private Long docId;

    /**
     * 课程标题
     */
    private String title;

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
     * 分类id
     */
    private Integer categoryId;

    private LocalDateTime createdTime;

    private LocalDateTime updatedTime;

    private String keyword;

    private BigDecimal score;

}
