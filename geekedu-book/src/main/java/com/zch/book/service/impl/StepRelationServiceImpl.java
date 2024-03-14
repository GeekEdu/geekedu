package com.zch.book.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.dto.path.RelationCourseForm;
import com.zch.book.domain.po.StepRelation;
import com.zch.book.enums.LearnPathCourseEnums;
import com.zch.book.mapper.StepRelationMapper;
import com.zch.book.service.IStepRelationService;
import com.zch.common.core.utils.CollUtils;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.common.core.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Poison02
 * @date 2024/3/13
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class StepRelationServiceImpl extends ServiceImpl<StepRelationMapper, StepRelation> implements IStepRelationService {

    @Transactional
    @Override
    public Boolean addRelation(Integer stepId, Integer pathId, List<RelationCourseForm> form) {
        if (ObjectUtils.isNull(form) || CollUtils.isEmpty(form)) {
            return true;
        }
        List<StepRelation> list = new ArrayList<>(form.size());
        for (RelationCourseForm item : form) {
            StepRelation relation = new StepRelation();
            relation.setRelationId(item.getRelationId());
            relation.setRelationType(LearnPathCourseEnums.valueOf(item.getType()));
            relation.setCover(item.getCover());
            relation.setPrice(StringUtils.isBlank(item.getPrice()) ? BigDecimal.ZERO : new BigDecimal(item.getPrice()));
            relation.setPathId(pathId);
            relation.setStepId(stepId);
            list.add(relation);
        }
        saveBatch(list);
        return true;
    }

    @Transactional
    @Override
    public Boolean updateRelation(Integer stepId, Integer pathId, List<RelationCourseForm> form) {
        if (ObjectUtils.isNull(form) || CollUtils.isEmpty(form)) {
            return true;
        }
        // 更新应该是：把原来的删了，然后插入新的
        remove(new LambdaQueryWrapper<StepRelation>()
                .eq(StepRelation::getStepId, stepId)
                .eq(StepRelation::getPathId, pathId));
        addRelation(stepId, pathId, form);
        return true;
    }
}
