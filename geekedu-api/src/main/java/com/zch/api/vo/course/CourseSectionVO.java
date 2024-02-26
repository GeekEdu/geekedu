package com.zch.api.vo.course;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author Poison02
 * @date 2024/2/26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CourseSectionVO extends BaseVO {

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
     * 视频链接
     */
    private String videoLink;

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

    private CourseChapterVO chapter;

    private CourseSimpleVO course;

}
