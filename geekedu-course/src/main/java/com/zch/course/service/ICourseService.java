package com.zch.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.dto.ask.CommentsBatchDelForm;
import com.zch.api.vo.course.CourseAndCategoryVO;
import com.zch.api.vo.course.CourseCommentsVO;
import com.zch.api.vo.course.CourseSimpleVO;
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

}
