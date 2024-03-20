package com.zch.course.controller;

import com.zch.api.dto.ask.AddCommentForm;
import com.zch.api.dto.ask.CommentsBatchDelForm;
import com.zch.api.dto.course.ChapterForm;
import com.zch.api.dto.course.live.LiveCourseForm;
import com.zch.api.dto.course.live.LiveVideoForm;
import com.zch.api.vo.ask.CommentsFullVO;
import com.zch.api.vo.course.CourseCommentsVO;
import com.zch.api.vo.course.live.*;
import com.zch.common.mvc.result.PageResult;
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

    /**
     * 获取直播课详情
     * @param courseId
     * @return
     */
    @GetMapping("/{id}/detail")
    public Response<LiveCourseVO> getLiveCourseDetail(@PathVariable("id") Integer courseId) {
        return Response.success(courseService.getLiveCourseDetail(courseId));
    }

    /**
     * 删除直播课
     * @param courseId
     * @return
     */
    @PostMapping("/{id}/delete")
    public Response<Boolean> deleteLiveCourse(@PathVariable("id") Integer courseId) {
        return Response.success(courseService.deleteLiveCourse(courseId));
    }

    /**
     * 更新直播课
     * @param courseId
     * @param form
     * @return
     */
    @PostMapping("/{id}/update")
    public Response<Boolean> updateLiveCourse(@PathVariable("id") Integer courseId, @RequestBody LiveCourseForm form) {
        return Response.success(courseService.updateLiveCourse(courseId, form));
    }

    /**
     * 新增直播课
     * @param form
     * @return
     */
    @PostMapping("/add")
    public Response<Boolean> addLiveCourse(LiveCourseForm form) {
        return Response.success(courseService.addLiveCourse(form));
    }

    /**
     * 直播课分类列表
     * @return
     */
    @GetMapping("/category/list")
    public Response<LiveCategoryVO> getCategoryList() {
        return Response.success(courseService.getCategoryList());
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
     * @param id
     * @return
     */
    @GetMapping("/chapter/getChapterById/{id}")
    public Response<LiveChapterVO> getChapterById(@PathVariable("id") Integer id) {
        return Response.success(courseService.getChapterById(id));
    }

    /**
     * 新增章节
     * @param form
     * @return
     */
    @PostMapping("/chapter/add")
    public Response<Boolean> addChapter(@RequestBody ChapterForm form) {
        return Response.success(courseService.addChapter(form));
    }

    /**
     * 更新章节
     * @param id
     * @param form
     * @return
     */
    @PostMapping("/chapter/update/{id}")
    public Response<Boolean> updateChapter(@PathVariable("id") Integer id, @RequestBody ChapterForm form) {
        return Response.success(courseService.updateChapter(id, form));
    }

    /**
     * 删除章节
     * @param id
     * @return
     */
    @PostMapping("/chapter/delete/{id}")
    public Response<Boolean> deleteChapter(@PathVariable("id") Integer id) {
        return Response.success(courseService.deleteChapterById(id));
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
    public Response<CourseCommentsVO> getCourseComments(@RequestParam("pageNum") Integer pageNum,
                                                        @RequestParam("pageSize") Integer pageSize,
                                                        @RequestParam("cType") String cType,
                                                        @RequestParam(value = "createdTime", required = false) List<String> createdTime) {
        return Response.success(courseService.getCommentsList(pageNum, pageSize, cType, createdTime));
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
     * 根据课程id返回简单课程
     * @param id
     * @return
     */
    @GetMapping("/getCourseSimpleById/{id}")
    public Response<LiveCourseSimpleVO> getCourseSimpleById(@PathVariable("id") Integer id) {
        return Response.success(courseService.getCourseSimpleById(id));
    }

    /**
     * 分页查找视频列表
     * @param pageNum
     * @param pageSize
     * @param courseId
     * @return
     */
    @GetMapping("/video/list")
    public PageResult<LiveVideoVO> getVideoList(@RequestParam("pageNum") Integer pageNum,
                                     @RequestParam("pageSize") Integer pageSize,
                                     @RequestParam("courseId") Integer courseId) {
        return PageResult.success(courseService.getVideoList(pageNum, pageSize, courseId));
    }

    /**
     * 视频明细
     * @param id
     * @return
     */
    @GetMapping("/video/{id}/detail")
    public Response<LiveVideoVO> getVideoDetail(@PathVariable("id") Integer id) {
        return Response.success(courseService.getVideoDetail(id));
    }

    /**
     * 删除视频
     * @param id
     * @return
     */
    @PostMapping("/video/{id}/delete")
    public Response<Boolean> deleteVideo(@PathVariable("id") Integer id) {
        return Response.success(courseService.deleteVideo(id));
    }

    /**
     * 更新视频
     * @param id
     * @param form
     * @return
     */
    @PostMapping("/video/{id}/update")
    public Response<Boolean> updateVideo(@PathVariable("id") Integer id, @RequestBody LiveVideoForm form) {
        return Response.success(courseService.updateVideo(id, form));
    }

    /**
     * 新增视频
     * @param form
     * @return
     */
    @PostMapping("/video/add")
    public Response<Boolean> addVideo(@RequestBody LiveVideoForm form) {
        return Response.success(courseService.addVideo(form));
    }

    //==========================================================================

    /**
     * 前台 直播课列表
     * @param pageNum
     * @param pageSize
     * @param categoryId
     * @return
     */
    @GetMapping("/v2/list")
    public Response<LiveFrontVO> getV2List(@RequestParam("pageNum") Integer pageNum,
                                @RequestParam("pageSize") Integer pageSize,
                                @RequestParam("categoryId") Integer categoryId) {
        return Response.success(courseService.getV2List(pageNum, pageSize, categoryId));
    }

    /**
     * 获取直播详情
     * @param id
     * @return
     */
    @GetMapping("/v2/{id}/detail")
    public Response<LiveDetailVO> getV2Detail(@PathVariable("id") Integer id) {
        return Response.success(courseService.getLiveDetail(id));
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
     * 评论课程
     * @param id
     * @param form
     * @return
     */
    @PostMapping("/v2/{id}/comment")
    public Response<Boolean> addCourseComment(@PathVariable("id") Integer id, @RequestBody AddCommentForm form) {
        return Response.success(courseService.addCourseComment(id, form));
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

}
