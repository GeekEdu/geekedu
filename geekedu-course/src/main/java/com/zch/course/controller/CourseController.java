package com.zch.course.controller;

import com.zch.api.dto.ask.CommentsBatchDelForm;
import com.zch.api.dto.course.ChapterForm;
import com.zch.api.vo.course.CourseChapterVO;
import com.zch.api.vo.course.CourseSimpleVO;
import com.zch.common.mvc.result.Response;
import com.zch.course.service.ICourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    /**
     * 分页查找课程评论列表
     * @param pageNum
     * @param pageSize
     * @param cType
     * @param createdTime
     * @return
     */
    @GetMapping("/getCommentsList")
    public Response getCourseComments(@RequestParam("pageNum") Integer pageNum,
                                      @RequestParam("pageSize") Integer pageSize,
                                      @RequestParam("cType") String cType,
                                      @RequestParam(value = "createdTime", required = false) List<String> createdTime) {
        return Response.success(courseService.getCommentsList(pageNum, pageSize, cType, createdTime));
    }

    /**
     * 根据课程id返回简单课程
     * @param id
     * @return
     */
    @GetMapping("/getCourseSimpleById/{id}")
    public Response<CourseSimpleVO> getCourseSimpleById(@PathVariable("id") Integer id) {
        return Response.success(courseService.getCourseSimpleById(id));
    }

    /**
     * 批量删除课程评论
     * @param form
     * @return
     */
    @PostMapping("/comments/delete/batch")
    public Response<Boolean> deleteCourseCommentsBatch(@RequestBody CommentsBatchDelForm form) {
        return Response.success(courseService.deleteBatchCourseComments(form));
    }

    /**
     * 返回章节列表
     * @param courseId
     * @return
     */
    @GetMapping("/{id}/chapter/getChapterList")
    public Response<List<CourseChapterVO>> getChapterList(@PathVariable("id") Integer courseId) {
        return Response.success(courseService.getChapterList(courseId));
    }

    /**
     * 获取章节明细
     * @param courseId
     * @param id
     * @return
     */
    @GetMapping("/{cId}/chapter/getChapterById/{id}")
    public Response<CourseChapterVO> getChapterById(@PathVariable("cId") Integer courseId, @PathVariable("id") Integer id) {
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
