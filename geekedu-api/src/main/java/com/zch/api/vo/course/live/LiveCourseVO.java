package com.zch.api.vo.course.live;

import com.zch.api.vo.label.CategorySimpleVO;
import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Poison02
 * @date 2024/3/12
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LiveCourseVO extends BaseVO {

    private Integer id;

    /**
     * 标题
     */
    private String title;

    /**
     * 封面
     */
    private String cover;

    /**
     * 简介
     */
    private String intro;

    /**
     * 详情
     */
    private String renderDesc;

    /**
     * 分类id
     */
    private Integer categoryId;

    /**
     * 是否展示
     */
    private Boolean isShow;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 直播小节数
     */
    private Integer videosCount;

    /**
     * 章节数
     */
    private Integer chapterCount;

    /**
     * 评论数
     */
    private Integer commentsCount;

    /**
     * vip是否免费观看
     */
    private Boolean vipCanView;

    /**
     * 学员数
     */
    private Long userCount;

    /**
     * 教师id
     */
    private Long teacherId;

    /**
     * 课程状态 文案
     */
    private String statusText;

    /**
     * 上架时间
     */
    private LocalDateTime groundingTime;

    /**
     * 分类信息
     */
    private CategorySimpleVO category;

}
