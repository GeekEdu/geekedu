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

    /**
     * 分页查找课程列表
     * @param pageNum 当前页码
     * @param pageSize 每页记录数
     * @param sort 排序字段
     * @param order 排序方式 desc asc
     * @param keywords 课程名称关键字
     * @param cid 分类id
     * @param id 课程id
     * @return
     */
    @GetMapping("/courses")
    public Response getCourses(@RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize, @RequestParam("sort") String sort, @RequestParam("order") String order,
                               @RequestParam(value = "keywords", required = false) String keywords,
                               @RequestParam(value = "cid", required = false) Integer cid,
                               @RequestParam(value = "id", required = false) Integer id) {
        return Response.success(courseService.getCoursePage(pageNum, pageSize, sort, order, keywords, cid, id));
    }

}
