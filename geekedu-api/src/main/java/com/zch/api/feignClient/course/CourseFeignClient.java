package com.zch.api.feignClient.course;

import com.zch.api.interceptor.FeignInterceptor;
import com.zch.api.vo.course.CourseSimpleVO;
import com.zch.api.vo.course.CourseVO;
import com.zch.api.vo.course.live.LiveCourseSimpleVO;
import com.zch.api.vo.course.live.LiveCourseVO;
import com.zch.api.vo.system.search.SearchFullVO;
import com.zch.common.mvc.result.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

/**
 * @author Poison02
 * @date 2024/2/26
 */
@FeignClient(contextId = "course", value = "course-service", configuration = FeignInterceptor.class)
public interface CourseFeignClient {

    @GetMapping("/api/getCourseSimpleById/{id}")
    Response<CourseSimpleVO> getCourseSimpleById(@PathVariable("id") Integer id);

    /**
     * 根据课程id获取课程明细
     * @param id
     * @return
     */
    @GetMapping("/api/getCourseById/{id}")
    Response<CourseVO> getCourseById(@PathVariable("id") Integer id);

    /**
     * 根据课程id返回简单直播课程
     * @param id
     * @return
     */
    @GetMapping("/api/live/course/getCourseSimpleById/{id}")
    Response<LiveCourseSimpleVO> getLiveCourseSimpleById(@PathVariable("id") Integer id);

    /**
     * 获取直播课详情
     * @param courseId
     * @return
     */
    @GetMapping("/api/live/course/{id}/detail")
    Response<LiveCourseVO> getLiveCourseDetail(@PathVariable("id") Integer courseId);

    /**
     * 查询课程价格
     * @param id
     * @return
     */
    @PostMapping("/api/v2/{id}/price")
    Response<BigDecimal> queryCoursePrice(@PathVariable("id") Integer id);

    /**
     * 全文检索课程
     * @param offset
     * @param limit
     * @param type
     * @param keyword
     * @return
     */
    @GetMapping("/api/v2/search")
    Response<SearchFullVO> searchCourse(@RequestParam("offset") Integer offset,
                                                 @RequestParam("limit") Integer limit,
                                                 @RequestParam("type") String type,
                                                 @RequestParam("keyword") String keyword);

}
