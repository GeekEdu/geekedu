package com.zch.book.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.dto.path.RelationCourseForm;
import com.zch.book.domain.po.StepRelation;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/3/13
 */
public interface IStepRelationService extends IService<StepRelation> {

    /**
     * 添加关联
     * @param form
     * @return
     */
    Boolean addRelation(Integer stepId, Integer pathId, List<RelationCourseForm> form);

    /**
     * 更新关联
     * @param form
     * @return
     */
    Boolean updateRelation(Integer stepId, Integer pathId, List<RelationCourseForm> form);

}
