package com.zch.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.dto.course.ChapterForm;
import com.zch.api.vo.course.CourseChapterVO;
import com.zch.course.domain.po.CourseChapter;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/2/26
 */
public interface ICourseChapterService extends IService<CourseChapter> {

    /**
     * 查找某个课程的章节列表
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

}
