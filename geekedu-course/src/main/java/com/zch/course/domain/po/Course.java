package com.zch.course.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
     * 课程名
     */
    private String name;

    /**
     * 课程售卖类型 0-免费 1-收费
     */
    private Boolean sellType;

    /**
     * 课程价格
     */
    private BigDecimal price;

    /**
     * 课程状态 1-待上架 2-已上架 3-已下架 4-已完结
     */
    private Boolean status;

    /**
     * 课程评分 最高50分
     */
    private Short score;

    /**
     * 课程总时长，单位 秒
     */
    private Long duration;

    /**
     * 课程总小节数
     */
    private Integer sectionNum;

    /**
     * 课程 教师id
     */
    private Long teacher;

    /**
     * 课程简介
     */
    private String intro;

    /**
     * 课程描述
     */
    private String description;

    /**
     * 课程封面
     */
    private String pictureLink;

    /**
     * 课程卖出数量
     */
    private Integer sellNum;

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
    private Boolean isDelete;

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

}
