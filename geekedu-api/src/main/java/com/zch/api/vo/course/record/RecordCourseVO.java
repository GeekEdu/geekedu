package com.zch.api.vo.course.record;

import com.zch.api.vo.course.CourseChapterVO;
import com.zch.api.vo.course.CourseSectionVO;
import com.zch.api.vo.course.CourseVO;
import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Poison02
 * @date 2024/3/5
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RecordCourseVO extends BaseVO {

    /**
     * 课程数据
     */
    private CourseVO course;

    /**
     * 章节
     */
    private List<CourseChapterVO> chapters = new ArrayList<>(0);

    /**
     * 视频
     */
    private Map<Integer, List<CourseSectionVO>> videos = new HashMap<>(0);

    /**
     * 是否购买课程
     */
    private Boolean isBuy = false;

    /**
     * 是否收藏课程
     */
    private Boolean isCollect = false;

    private Boolean isVip = false;

    /**
     * 附件
     */
    private List<String> attach = new ArrayList<>(0);

    /**
     * 视频观看进度
     */
    private Map<Integer, LearnRecordVO> videoWatchedProgress = new HashMap<>(0);

}
