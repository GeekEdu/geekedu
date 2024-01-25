package com.zch.course.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zch.common.mvc.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
    private Long courseId;

    /**
     * 小节标题
     */
    private String title;

    /**
     * 视频链接
     */
    private String videoLink;

    /**
     * 观看次数
     */
    private Long count;

    /**
     * 章节 id
     */
    private Integer chapterId;

    private Long createdBy;

    private Long updatedBy;

    private Boolean isDelete;

}
