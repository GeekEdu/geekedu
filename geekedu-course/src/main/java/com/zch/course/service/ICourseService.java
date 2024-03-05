package com.zch.course.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.dto.ask.AddCommentForm;
import com.zch.api.dto.ask.CommentsBatchDelForm;
import com.zch.api.dto.course.ChapterForm;
import com.zch.api.dto.course.DelSectionBatchForm;
import com.zch.api.vo.ask.CommentsFullVO;
import com.zch.api.vo.course.*;
import com.zch.api.vo.course.record.PlayUrlVO;
import com.zch.api.vo.course.record.RecordCourseVO;
import com.zch.api.vo.course.record.RecordSectionVO;
import com.zch.api.vo.label.CategorySimpleVO;
import com.zch.course.domain.po.Course;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/1/25
 */
public interface ICourseService extends IService<Course> {

    /**
     * 查询课程列表
     * @param pageNum 页码
     * @param pageSize 每页记录数
     * @param sort 排序字段
     * @param order 排序方式 asc desc
     * @param keywords 关键词
     * @param cid 分类id
     * @param id id
     * @return
     */
    CourseAndCategoryVO getCoursePage(Integer pageNum, Integer pageSize, String sort, String order, String keywords, Integer cid, Integer id);

    /**
     * 根据课程id获取课程明细
     * @param id
     * @return
     */
    CourseVO getCourseById(Integer id);

    /**
     * 根据id删除课程
     * @param id
     * @return
     */
    Boolean deleteCourseById(Integer id);

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
    CourseSimpleVO getCourseSimpleById(Integer id);

    /**
     * 批量删除课程评论
     * @param form
     * @return
     */
    Boolean deleteBatchCourseComments(CommentsBatchDelForm form);

    /**
     * 获取章节列表
     * @param courseId
     * @return
     */
    List<CourseChapterVO> getChapterList(Integer courseId);

    /**
     * 根据章节id删除章节
     * @param id
     * @return
     */
    Boolean deleteChapterById(Integer courseId, Integer id);

    /**
     * 新增章节
     * @param courseId
     * @param form
     * @return
     */
    Boolean addChapter(Integer courseId, ChapterForm form);

    /**
     * 更新章节
     * @param courseId
     * @param id
     * @param form
     * @return
     */
    Boolean updateChapter(Integer courseId, Integer id, ChapterForm form);

    /**
     * 获取章节明细
     * @param courseId
     * @param id
     * @return
     */
    CourseChapterVO getChapterById(Integer courseId, Integer id);

    /**
     * 分页查课时列表
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param courseId
     * @return
     */
    Page<CourseSectionVO> getSectionList(Integer pageNum, Integer pageSize, String sort, String order, Integer courseId);

    /**
     * 根据id获取课时明细
     * @param id
     * @return
     */
    CourseSectionVO getSectionById(Integer id);

    /**
     * 批量删除课时
     * @param form
     * @return
     */
    Boolean deleteSectionBatch(DelSectionBatchForm form);

// ================================================================================================
// 前台
    /**
     * 获取简单分类列表
     * @return
     */
    List<CategorySimpleVO> getCategorySimpleList();

    /**
     * 条件分页查找录播课列表
     * @param pageNum
     * @param pageSize
     * @param scene
     * @param categoryId
     * @return
     */
    Page<CourseVO> getCourseCondition(Integer pageNum, Integer pageSize, String scene, Integer categoryId);

    /**
     * 返回课程明细
     * @param id
     * @return
     */
    RecordCourseVO getDetailCourse(Integer id);

    /**
     * 获取课程评论
     * @param courseId
     * @return
     */
    CommentsFullVO getCourseComments(Integer courseId);

    /**
     * 新增课程评论
     * @param id
     * @param form
     * @return
     */
    Boolean addCourseComment(Integer id, AddCommentForm form);

    /**
     * 前台 课时评论
     * @param sectionId
     * @return
     */
    CommentsFullVO getSectionComments(Integer sectionId);

    /**
     * 前台 课时明细
     * @param sectionId
     * @return
     */
    RecordSectionVO getSectionDetail(Integer sectionId);

    /**
     * 新增课时评论
     * @param sectionId
     * @param form
     * @return
     */
    Boolean addSectionComment(Integer sectionId, AddCommentForm form);

    /**
     * 获取课时播放地址
     * @param sectionId
     * @param isTry
     * @return
     */
    PlayUrlVO getSectionPlayUrl(Integer sectionId, Integer isTry);

}
