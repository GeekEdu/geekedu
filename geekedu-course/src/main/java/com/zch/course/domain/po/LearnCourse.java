package com.zch.course.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zch.common.mvc.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Poison02
 * @date 2024/3/20
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("learn_course")
public class LearnCourse extends BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer courseId;

    private Integer videoId;

    private Long userId;

    /**
     * 学习进度 0-100 学习课程数 / 总课程数
     */
    private Integer progress;

    /**
     * 是否订阅
     */
    private Boolean isSubscribe;

    private String type;

    private Integer learnCount;

    /**
     * 是否完结
     */
    private Boolean isOver;

    @TableLogic
    private Boolean isDelete;

}
