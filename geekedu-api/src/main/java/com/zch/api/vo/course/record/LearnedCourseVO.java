package com.zch.api.vo.course.record;

import com.zch.api.vo.course.CourseSimpleVO;
import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Poison02
 * @date 2024/3/30
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LearnedCourseVO extends BaseVO {

    /**
     * 是否已经完成本课程
     */
    private Boolean isOver;

    /**
     * 视频观看进度 使用观看小节数 / 总小节数 取整即可
     */
    private Integer progress;

    /**
     * 观看小节数
     */
    private Integer learnedCount = 0;

    /**
     * 上一次观看的小节
     */
    private LearnRecordVO lastViewVideo;

    /**
     * 课程信息
     */
    private CourseSimpleVO course;

}
