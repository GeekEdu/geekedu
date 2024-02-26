package com.zch.course.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zch.common.mvc.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Poison02
 * @date 2024/1/25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "course")
public class Course extends BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
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
     * 课程创建人
     */
    private Long createdBy;

    /**
     * 课程更新人
     */
    private Long updatedBy;

    /**
     * 课程是否被删除
     */
    @TableLogic
    private Boolean isDelete;

}
