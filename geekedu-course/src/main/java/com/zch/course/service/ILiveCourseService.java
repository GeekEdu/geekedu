package com.zch.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.dto.course.ChapterForm;
import com.zch.api.vo.course.live.LiveChapterVO;
import com.zch.api.vo.course.live.LiveCourseFullVO;
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
    LiveChapterVO getChapterById(Integer courseId, Integer id);

}
