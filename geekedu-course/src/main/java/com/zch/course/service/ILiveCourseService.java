package com.zch.course.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.dto.ask.AddCommentForm;
import com.zch.api.dto.ask.CommentsBatchDelForm;
import com.zch.api.dto.course.ChapterForm;
import com.zch.api.dto.course.live.LiveCourseForm;
import com.zch.api.dto.course.live.LiveVideoForm;
import com.zch.api.vo.ask.CommentsFullVO;
import com.zch.api.vo.course.CourseCommentsVO;
import com.zch.api.vo.course.live.*;
import com.zch.course.domain.po.LiveCourse;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/3/12
 */
public interface ILiveCourseService extends IService<LiveCourse> {

    /**
     * 条件分页查询直播课程列表
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param keywords
     * @param categoryId
     * @param teacherId
     * @param status
     * @return
     */
    LiveCourseFullVO getLiveCourseFullList(Integer pageNum, Integer pageSize, String sort, String order,
                                           String keywords, Integer categoryId, Long teacherId, Integer status);

    /**
     * 更新直播课
     * @param courseId
     * @param form
     * @return
     */
    Boolean updateLiveCourse(Integer courseId, LiveCourseForm form);

    /**
     * 新增直播课
     * @param form
     * @return
     */
    Boolean addLiveCourse(LiveCourseForm form);

    /**
     * 删除直播课
     * @param courseId
     * @return
     */
    Boolean deleteLiveCourse(Integer courseId);

    /**
     * 获取直播课明细
     * @param courseId
     * @return
     */
    LiveCourseVO getLiveCourseDetail(Integer courseId);

    /**
     * 获取分类列表
     * @return
     */
    LiveCategoryVO getCategoryList();

    /**
     * 获取章节列表
     * @param courseId
     * @return
     */
    List<LiveChapterVO> getChapterList(Integer courseId);

    /**
     * 根据章节id删除章节
     * @param id
     * @return
     */
    Boolean deleteChapterById(Integer id);

    /**
     * 新增章节
     * @param form
     * @return
     */
    Boolean addChapter(ChapterForm form);

    /**
     * 更新章节
     * @param id
     * @param form
     * @return
     */
    Boolean updateChapter(Integer id, ChapterForm form);

    /**
     * 获取章节明细
     * @param id
     * @return
     */
    LiveChapterVO getChapterById(Integer id);

    /**
     * 条件分页查找评论列表
     * @param pageNum
     * @param pageSize
     * @param cType
     * @param createdTime
     * @return
     */
    CourseCommentsVO getCommentsList(Integer pageNum, Integer pageSize, String cType, List<String> createdTime);

    /**
     * 根据id 返回简单课程信息
     * @param id
     * @return
     */
    LiveCourseSimpleVO getCourseSimpleById(Integer id);

    /**
     * 批量删除课程评论
     * @param form
     * @return
     */
    Boolean deleteBatchCourseComments(CommentsBatchDelForm form);

    /**
     * 分页查找视频列表
     * @param pageNum
     * @param pageSize
     * @param courseId
     * @return
     */
    Page<LiveVideoVO> getVideoList(Integer pageNum, Integer pageSize, Integer courseId);

    /**
     * 视频明细
     * @param id
     * @return
     */
    LiveVideoVO getVideoDetail(Integer id);

    /**
     * 更新视频
     * @param id
     * @param form
     * @return
     */
    Boolean updateVideo(Integer id, LiveVideoForm form);

    /**
     * 删除视频
     * @param id
     * @return
     */
    Boolean deleteVideo(Integer id);

    /**
     * 新增视频
     * @param form
     * @return
     */
    Boolean addVideo(LiveVideoForm form);

    //===================================================================
    /**
     * 获取V2课程列表
     * @param pageNum
     * @param pageSize
     * @param categoryId
     * @return
     */
    LiveFrontVO getV2List(Integer pageNum, Integer pageSize, Integer categoryId);

    /**
     * 获取直播详情
     * @param id
     * @return
     */
    LiveDetailVO getLiveDetail(Integer id);

    /**
     * 获取课程评论
     * @param courseId
     * @return
     */
    CommentsFullVO getCourseComments(Integer courseId);

    /**
     * 评论直播课程
     * @param id
     * @param form
     * @return
     */
    Boolean addCourseComment(Integer id, AddCommentForm form);

    /**
     * 获取直播信息
     * @param id
     * @return
     */
    LiveVO getPlayInfo(Integer id);

    /**
     * 课程收藏
     * @param id
     * @return
     */
    Boolean courseCollect(Integer id);

}
