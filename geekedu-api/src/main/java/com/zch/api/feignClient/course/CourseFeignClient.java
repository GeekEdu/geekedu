package com.zch.api.feignClient.course;

import com.zch.api.interceptor.FeignInterceptor;
import com.zch.api.vo.course.CourseSimpleVO;
import com.zch.api.vo.course.live.LiveCourseSimpleVO;
import com.zch.common.mvc.result.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Poison02
 * @date 2024/2/26
 */
@FeignClient(contextId = "course", value = "course-service", configuration = FeignInterceptor.class)
public interface CourseFeignClient {

    @GetMapping("/api/getCourseSimpleById/{id}")
    public Response<CourseSimpleVO> getCourseSimpleById(@PathVariable("id") Integer id);

    /**
     * 根据课程id返回简单直播课程
     * @param id
     * @return
     */
    @GetMapping("/api/live/course/getCourseSimpleById/{id}")
    public Response<LiveCourseSimpleVO> getLiveCourseSimpleById(@PathVariable("id") Integer id);

}
