package com.zch.course.job;

import com.xxl.job.core.handler.annotation.XxlJob;
import com.zch.common.core.utils.BeanUtils;
import com.zch.course.domain.dto.CourseMSDTO;
import com.zch.course.domain.dto.LiveCourseMSDTO;
import com.zch.course.domain.po.Course;
import com.zch.course.domain.po.LiveCourse;
import com.zch.course.service.ICourseService;
import com.zch.course.service.ILiveCourseService;
import com.zch.course.service.MeiliSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 同步录播课和直播课数据到MeiliSearch
 * @author Poison02
 * @date 2024/4/2
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class SyncMeiliSearchHandler {

    private final MeiliSearchService meiliSearchService;

    private final ICourseService courseService;

    private final ILiveCourseService liveCourseService;

    @XxlJob("syncMeiliSearchHandler")
    public void syncMeiliSearchHandler() {
        meiliSearchService.insertCourseIndex("course", "id");
        meiliSearchService.insertCourseIndex("liveCourse", "id");
        List<Course> course = courseService.list();
        for (Course item : course) {
            CourseMSDTO temp = new CourseMSDTO();
            BeanUtils.copyProperties(item, temp);
            meiliSearchService.insertCourseDocument(temp);
        }
        List<LiveCourse> live = liveCourseService.list();
        for (LiveCourse item : live) {
            LiveCourseMSDTO temp = new LiveCourseMSDTO();
            BeanUtils.copyProperties(item, temp);
            meiliSearchService.insertLiveCourseDocument(temp);
        }
    }

}
