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
@TableName("course_chapter")
public class CourseChapter extends BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 章节名
     */
    private String name;

    /**
     * 课程id
     */
    private Integer courseId;

    private Integer sort;

    private Long createdBy;

    private Long updatedBy;

    private Boolean isDelete;

}
