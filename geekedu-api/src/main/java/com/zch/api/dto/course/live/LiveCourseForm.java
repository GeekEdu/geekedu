package com.zch.api.dto.course.live;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Poison02
 * @date 2024/3/13
 */
@Data
public class LiveCourseForm implements Serializable {

    private static final Long serialVersionUID = 1L;

    /**
     * 分类id
     */
    private Integer categoryId;

    /**
     * 积分
     */
    private Integer price;

    /**
     * 是否显示
     */
    private Boolean isShow;

    /**
     * 简介
     */
    private String intro;

    /**
     * 详情
     */
    private String renderDesc;

    /**
     * 上架时间
     */
    private LocalDateTime groundingTime;

    /**
     * 教师id
     */
    private Long teacherId;

    /**
     * 封面
     */
    private String cover;

    /**
     * 标题
     */
    private String title;

    /**
     * vip免费观看
     */
    private Boolean vipCanView;

}
