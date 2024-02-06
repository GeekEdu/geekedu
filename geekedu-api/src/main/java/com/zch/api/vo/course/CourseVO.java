package com.zch.api.vo.course;

import com.zch.api.vo.label.CategoryVO;
import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Poison02
 * @date 2024/2/6
 */
@EqualsAndHashCode(callSuper = true)
@Data
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
     * 售卖类型 0-免费 1-收费
     */
    private Boolean sellType;

    /**
     * 课程状态;1-待上架，2-已上架，3-已下架，4-已完结
     */
    private Boolean status;

    /**
     * 课程价格
     */
    private BigDecimal price;

    /**
     * 课程视频时长 秒为单位
     */
    private Long duration;

    /**
     * 课程章节数
     */
    private Integer sectionNum;

    /**
     * 授课教师
     */
    private Long teacher;

    /**
     * 课程简介
     */
    private String intro;

    /**
     * 课程封面链接
     */
    private String pictureLink;

    /**
     * 课程销售量
     */
    private Integer sellNum;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 修改时间
     */
    private LocalDateTime updatedTime;

    /**
     * 上架时间
     */
    private LocalDateTime groundingTime;

    /**
     * 是否展示 0-不展示 1-展示
     */
    private Boolean isShow;

    /**
     * 课程类型 0-1录播 1-直播
     */
    private Boolean type;

    /**
     * 所有课程分类
     */
    private CategoryVO category;

}
