package com.zch.api.vo.course;

import com.zch.api.vo.label.CategorySimpleVO;
import com.zch.common.mvc.entity.BaseVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Poison02
 * @date 2024/2/6
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseVO extends BaseVO {

    /**
     * 课程id
     */
    private Integer id;

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
     * 课程卖出数量
     */
    private Integer sellNum;

    /**
     * 课时数量
     */
    private Integer sectionCount;

    /**
     * 章节数量
     */
    private Integer chapterCount;

    /**
     * 评论数量
     */
    private Integer commentsCount;;

    /**
     * 学员数量
     */
    private Integer userCount;

    /**
     * 上架时间
     */
    private LocalDateTime groundingTime;

    /**
     * 是否显示
     */
    private Boolean isShow;

    /**
     * 课程类型，0-录播课，1-直播课
     */
    private Boolean type;

    /**
     * 分类id
     */
    private Integer categoryId;

    /**
     * 课程所属分类
     */
    private CategorySimpleVO category;
}
