package com.zch.course.controller;

import com.zch.common.mvc.result.Response;
import com.zch.course.service.ICourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Poison02
 * @date 2024/2/6
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CourseController {

    private final ICourseService courseService;

    @GetMapping("/courses")
    public Response getCourses(@RequestParam(value = "pageNum", required = true) Integer pageNum, @RequestParam(value = "pageSize", required = true) Integer pageSize,
                               @RequestParam(value = "sort", required = false) String sort, @RequestParam(value = "order", required = false) String order, @RequestParam(value = "keywords", required = false) String keywords,
                               @RequestParam(value = "cid", required = false) Integer cid, @RequestParam(value = "id", required = false) Integer id) {
        return Response.success(courseService.getCoursePage(pageNum, pageSize, sort, order, keywords, cid, id));
    }

}
