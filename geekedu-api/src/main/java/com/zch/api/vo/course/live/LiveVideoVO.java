package com.zch.api.vo.course.live;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author Poison02
 * @date 2024/3/12
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LiveVideoVO extends BaseVO {

    private Integer id;

    /**
     * 标题
     */
    private String title;

    /**
     * 课程id
     */
    private Integer courseId;

    /**
     * 章节id
     */
    private Integer chapterId;

    /**
     * 是否显示
     */
    private Boolean isShow;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 状态 文本
     */
    private String statusText;

    /**
     * 直播时间
     */
    private LocalDateTime liveTime;

    /**
     * 录播id
     */
    private String recordId;

    /**
     * 预估直播时间 秒
     */
    private Long estimateDuration;

    /**
     * 实际直播时间 秒
     */
    private Long duration;

    /**
     * 观看次数
     */
    private Integer viewCount;

    /**
     * 章节信息
     */
    private LiveChapterVO chapter;

    /**
     * 课程信息
     */
    private LiveCourseSimpleVO course;

}
