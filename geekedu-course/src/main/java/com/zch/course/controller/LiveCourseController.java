package com.zch.course.controller;

import com.zch.api.dto.course.ChapterForm;
import com.zch.api.vo.course.live.LiveChapterVO;
import com.zch.api.vo.course.live.LiveCourseFullVO;
import com.zch.api.vo.course.live.LiveCourseVO;
import com.zch.common.mvc.result.Response;
import com.zch.course.service.ILiveCourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/3/12
 */
@RestController
@RequestMapping("/api/live/course")
@RequiredArgsConstructor
public class LiveCourseController {

    private final ILiveCourseService courseService;

    /**
     * 后台 条件分页查询直播课程列表
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param keywords
     * @param categoryId
     * @param status
     * @param teacherId
     * @return
     */
    @GetMapping("/list")
    public Response<LiveCourseFullVO> getLiveCourseList(@RequestParam("pageNum") Integer pageNum,
                                                        @RequestParam("pageSize") Integer pageSize,
                                                        @RequestParam("sort") String sort,
                                                        @RequestParam("order") String order,
                                                        @RequestParam(value = "keywords", required = false) String keywords,
                                                        @RequestParam(value = "categoryId", required = false) Integer categoryId,
                                                        @RequestParam(value = "status", required = false) Integer status,
                                                        @RequestParam(value = "teacherId", required = false) Long teacherId) {
        return Response.success(courseService.getLiveCourseFullList(pageNum, pageSize, sort, order, keywords, categoryId, teacherId, status));
    }

    @GetMapping("/{id}/detail")
    public Response<LiveCourseVO> getLiveCourseDetail(@PathVariable("id") Integer courseId) {
        return Response.success();
    }

    @PostMapping("/{id}/delete")
    public Response<Boolean> deleteLiveCourse(@PathVariable("id") Integer courseId) {
        return Response.success();
    }

    @PostMapping("/{id}/update")
    public Response<Boolean> updateLiveCourse(@PathVariable("id") Integer courseId) {
        return Response.success();
    }

    @PostMapping("/add")
    public Response<Boolean> addLiveCourse() {
        return Response.success();
    }

    /**
     * 返回章节列表
     * @param courseId
     * @return
     */
    @GetMapping("/{id}/chapter/getChapterList")
    public Response<List<LiveChapterVO>> getChapterList(@PathVariable("id") Integer courseId) {
        return Response.success(courseService.getChapterList(courseId));
    }

    /**
     * 获取章节明细
     * @param courseId
     * @param id
     * @return
     */
    @GetMapping("/{cId}/chapter/getChapterById/{id}")
    public Response<LiveChapterVO> getChapterById(@PathVariable("cId") Integer courseId, @PathVariable("id") Integer id) {
        return Response.success(courseService.getChapterById(courseId, id));
    }

    /**
     * 新增章节
     * @param courseId
     * @param form
     * @return
     */
    @PostMapping("/{cId}/chapter/add")
    public Response<Boolean> addChapter(@PathVariable("cId") Integer courseId, @RequestBody ChapterForm form) {
        return Response.success(courseService.addChapter(courseId, form));
    }

    /**
     * 更新章节
     * @param courseId
     * @param id
     * @param form
     * @return
     */
    @PostMapping("/{cId}/chapter/update/{id}")
    public Response<Boolean> updateChapter(@PathVariable("cId") Integer courseId, @PathVariable("id") Integer id, @RequestBody ChapterForm form) {
        return Response.success(courseService.updateChapter(courseId, id, form));
    }

    /**
     * 删除章节
     * @param courseId
     * @param id
     * @return
     */
    @PostMapping("/{cId}/chapter/delete/{id}")
    public Response<Boolean> deleteChapter(@PathVariable("cId") Integer courseId, @PathVariable("id") Integer id) {
        return Response.success(courseService.deleteChapterById(courseId, id));
    }

}
