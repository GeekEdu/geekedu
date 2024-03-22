package com.zch.course.controller;

import com.zch.api.dto.ask.AddCommentForm;
import com.zch.api.dto.ask.CommentsBatchDelForm;
import com.zch.api.dto.course.ChapterForm;
import com.zch.api.dto.course.DelSectionBatchForm;
import com.zch.api.dto.course.LearnRecordForm;
import com.zch.api.dto.course.vod.CourseForm;
import com.zch.api.vo.ask.CommentsFullVO;
import com.zch.api.vo.course.*;
import com.zch.api.vo.course.record.PlayUrlVO;
import com.zch.api.vo.course.record.RecordCourseVO;
import com.zch.api.vo.course.record.RecordSectionVO;
import com.zch.api.vo.label.CategorySimpleVO;
import com.zch.common.mvc.result.PageResult;
import com.zch.common.mvc.result.Response;
import com.zch.course.domain.repository.CourseInfoEs;
import com.zch.course.service.ICourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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
    public Response<CourseAndCategoryVO> getCourses(@RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize, @RequestParam("sort") String sort, @RequestParam("order") String order,
                                                    @RequestParam(value = "keywords", required = false) String keywords,
                                                    @RequestParam(value = "cid", required = false) Integer cid,
                                                    @RequestParam(value = "id", required = false) Integer id) {
        return Response.success(courseService.getCoursePage(pageNum, pageSize, sort, order, keywords, cid, id));
    }

    /**
     * 根据课程id获取课程明细
     * @param id
     * @return
     */
    @GetMapping("/getCourseById/{id}")
    public Response<CourseVO> getCourseById(@PathVariable("id") Integer id) {
        return Response.success(courseService.getCourseById(id));
    }

    /**
     * 新增课程
     * @param form
     * @return
     */
    @PostMapping("/add")
    public Response<Boolean> addCourse(@RequestBody CourseForm form) {
        return Response.success(courseService.addCourse(form));
    }

    /**
     * 根据id删除课程
     * @param id
     * @return
     */
    @PostMapping("/delete/{id}")
    public Response<Boolean> deleteCourseById(@PathVariable("id") Integer id) {
        return Response.success(courseService.deleteCourseById(id));
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

    /**
     * 分页查找课时列表
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param courseId
     * @return
     */
    @GetMapping("/section/getSectionList")
    public PageResult<CourseSectionVO> getSectionList(@RequestParam("pageNum") Integer pageNum,
                                                      @RequestParam("pageSize") Integer pageSize,
                                                      @RequestParam("sort") String sort,
                                                      @RequestParam("order") String order,
                                                      @RequestParam("courseId") Integer courseId) {
        return PageResult.success(courseService.getSectionList(pageNum, pageSize, sort, order, courseId));
    }

    /**
     * 根据课时id获取课时明细
     * @param id
     * @return
     */
    @GetMapping("/section/getSectionById/{id}")
    public Response<CourseSectionVO> getSectionBySectionId(@PathVariable("id") Integer id) {
        return Response.success(courseService.getSectionById(id));
    }

    /**
     * 批量删除课时
     * @param form
     * @return
     */
    @PostMapping("/section/delete/batch")
    public Response<Boolean> deleteSectionBatch(@RequestBody DelSectionBatchForm form) {
        return Response.success(courseService.deleteSectionBatch(form));
    }

    //=======================================================================================

    /**
     * 前台获取录播课分类列表
     * @return
     */
    @GetMapping("/v2/category/list")
    public Response<List<CategorySimpleVO>> getCourseCategoryList() {
        return Response.success(courseService.getCategorySimpleList());
    }

    /**
     * 前台分页查找录播课列表
     * @param pageNum
     * @param pageSize
     * @param scene 分为免费和热门，free
     * @param categoryId
     * @return
     */
    @GetMapping("/v2/courses")
    public PageResult getCourseCondition(@RequestParam("pageNum") Integer pageNum,
                                         @RequestParam("pageSize") Integer pageSize,
                                         @RequestParam(value = "scene", required = false) String scene,
                                         @RequestParam("categoryId") Integer categoryId) {
        return PageResult.success(courseService.getCourseCondition(pageNum, pageSize, scene, categoryId));
    }

    /**
     * 返回课程明细
     * @param id
     * @return
     */
    @GetMapping("/v2/detail/{id}")
    public Response<RecordCourseVO> getCourseDetail(@PathVariable("id") Integer id) {
        return Response.success(courseService.getDetailCourse(id));
    }

    /**
     * 返回课程评论
     * @param id
     * @return
     */
    @GetMapping("/v2/{id}/comments")
    public Response<CommentsFullVO> getCourseComments(@PathVariable("id") Integer id) {
        return Response.success(courseService.getCourseComments(id));
    }

    /**
     * 新增课程评论
     * @param id
     * @return
     */
    @PostMapping("/v2/{id}/comment")
    public Response<Boolean> addCourseComment(@PathVariable("id") Integer id,
                                              @RequestBody AddCommentForm form) {
        return Response.success(courseService.addCourseComment(id, form));
    }

    /**
     * 前台 课时评论
     * @param id
     * @return
     */
    @GetMapping("/v2/section/{id}/comments")
    public Response<CommentsFullVO> getSectionComments(@PathVariable("id") Integer id) {
        return Response.success(courseService.getSectionComments(id));
    }

    /**
     * 前台 课时信息
     * @param id
     * @return
     */
    @GetMapping("/v2/section/detail/{id}")
    public Response<RecordSectionVO> getSectionDetail(@PathVariable("id") Integer id) {
        return Response.success(courseService.getSectionDetail(id));
    }

    /**
     * 新增课时评论
     * @param id
     * @param form
     * @return
     */
    @PostMapping("/v2/section/{id}/comment")
    public Response<Boolean> addSectionComment(@PathVariable("id") Integer id,
                                               @RequestBody AddCommentForm form) {
        return Response.success(courseService.addSectionComment(id, form));
    }

    /**
     * 获取课时播放地址
     * @param id
     * @param isTry
     * @return
     */
    @GetMapping("/v2/section/{id}/playUrl")
    public Response<PlayUrlVO> getSectionPlayUrl(@PathVariable("id") Integer id,
                                                 @RequestParam("isTry") Integer isTry) {
        return Response.success(courseService.getSectionPlayUrl(id, isTry));
    }

    /**
     * 记录学习时长
     * @param id
     * @param form
     * @return
     */
    @PostMapping("/v2/{id}/video/record")
    public Response<Boolean> recordVideo(@PathVariable("id") Integer id, @RequestBody LearnRecordForm form) {
        return Response.success(courseService.courseRecord(id, form));
    }

    /**
     * 课程收藏
     * @param id
     * @return
     */
    @PostMapping("/v2/{id}/collect")
    public Response<Boolean> courseCollect(@PathVariable("id") Integer id) {
        return Response.success(courseService.courseCollect(id));
    }

    /**
     * 查询课程价格
     * @param id
     * @return
     */
    @PostMapping("/v2/{id}/price")
    public Response<BigDecimal> queryCoursePrice(@PathVariable("id") Integer id) {
        return Response.success(courseService.queryCoursePrice(id));
    }

    @GetMapping("/v2/search")
    public Response<List<CourseInfoEs>> esSearchCourse(@RequestParam("keyword") String keyword) {
        return Response.success(courseService.searchCourse(keyword));
    }

}
