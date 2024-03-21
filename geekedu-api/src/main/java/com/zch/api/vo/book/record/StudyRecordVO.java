package com.zch.api.vo.book.record;

import com.zch.api.vo.book.EBookVO;
import com.zch.api.vo.book.ImageTextVO;
import com.zch.api.vo.course.CourseVO;
import com.zch.api.vo.course.live.LiveCourseVO;
import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author Poison02
 * @date 2024/3/21
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StudyRecordVO extends BaseVO {

    private Integer id;

    /**
     * 上次浏览时间
     */
    private LocalDateTime lastViewTime;

    /**
     * 收藏时间
     */
    private LocalDateTime collectTime;

    /**
     * 订阅时间
     */
    private LocalDateTime subscribeTim;

    /**
     * 是否订阅
     */
    private Boolean isSubscribe;

    private Long userId;

    private Integer topicId;

    private ImageTextVO topic = new ImageTextVO();

    private Integer bookId;

    private EBookVO book;

    private Integer courseId;

    private CourseVO course;

    private Integer liveCourseId;

    private LiveCourseVO liveCourse;

}
