package com.zch.course.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.dto.course.DelSectionBatchForm;
import com.zch.api.vo.course.CourseSectionVO;
import com.zch.course.domain.po.CourseSection;

/**
 * @author Poison02
 * @date 2024/2/26
 */
public interface ICourseSectionService extends IService<CourseSection> {

    /**
     * 条件分页查找课时列表
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param courseId
     * @return
     */
    Page<CourseSectionVO> getSectionsPage(Integer pageNum, Integer pageSize, String sort, String order, Integer courseId);

    /**
     * 获取课时明细
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

}
