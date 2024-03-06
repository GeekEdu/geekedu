package com.zch.course.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zch.common.mvc.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author Poison02
 * @date 2024/1/25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("course_section")
public class CourseSection extends BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 课程id
     */
    private Integer courseId;

    /**
     * 小节标题
     */
    private String title;

    /**
     * 视频id
     */
    private Integer videoId;

    /**
     * 浏览次数
     */
    private Long viewCount;

    /**
     * 章节 id
     */
    private Integer chapterId;

    /**
     * 是否展示 0-否 1-是
     */
    private Boolean isShow;

    /**
     * 腾讯云id
     */
    private String tencentId;

    /**
     * 使用禁用快进 0-否 1-是
     */
    private Boolean banDrag;

    /**
     * 课时时长
     */
    private Long duration;

    /**
     * 试看时长
     */
    private Long freeSeconds;

    private LocalDateTime groundingTime;

    private Long createdBy;

    private Long updatedBy;

    @TableLogic
    private Boolean isDelete;

}
