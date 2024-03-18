package com.zch.book.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.dto.path.RelationCourseForm;
import com.zch.api.dto.path.StepForm;
import com.zch.api.feignClient.book.BookFeignClient;
import com.zch.api.feignClient.course.CourseFeignClient;
import com.zch.api.vo.book.EBookSimpleVO;
import com.zch.api.vo.book.ImageTextSimpleVO;
import com.zch.api.vo.course.CourseSimpleVO;
import com.zch.api.vo.course.live.LiveCourseSimpleVO;
import com.zch.api.vo.path.StepEndVO;
import com.zch.api.vo.path.StepVO;
import com.zch.book.domain.po.StepRelation;
import com.zch.book.domain.po.Steps;
import com.zch.book.mapper.StepsMapper;
import com.zch.book.service.IStepRelationService;
import com.zch.book.service.IStepsService;
import com.zch.common.core.utils.BeanUtils;
import com.zch.common.core.utils.CollUtils;
import com.zch.common.core.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.zch.book.enums.LearnPathCourseEnums.*;

/**
 * @author Poison02
 * @date 2024/3/13
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class StepsServiceImpl extends ServiceImpl<StepsMapper, Steps> implements IStepsService {

    private final IStepRelationService relationService;

    private final CourseFeignClient courseFeignClient;

    private final BookFeignClient bookFeignClient;

    @Override
    public List<StepVO> getStepList(Integer pathId) {
        List<Steps> list = list(new LambdaQueryWrapper<Steps>()
                .eq(Steps::getPathId, pathId));
        if (ObjectUtils.isNull(list) || CollUtils.isEmpty(list)) {
            return new ArrayList<>(0);
        }
        return list.stream().map(item -> {
            StepVO vo = new StepVO();
            BeanUtils.copyProperties(item, vo);
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public Boolean deleteStepById(Integer id) {
        return removeById(id);
    }

    @Override
    public StepEndVO getStepDetail(Integer id) {
        StepEndVO vo = new StepEndVO();
        Steps steps = getById(id);
        if (ObjectUtils.isNull(steps)) {
            return vo;
        }
        StepVO vo1 = new StepVO();
        BeanUtils.copyProperties(steps, vo1);
        vo.setStep(vo1);
        // 查询步骤关联的课程
        List<StepRelation> relations = relationService.list(new LambdaQueryWrapper<StepRelation>()
                .eq(StepRelation::getPathId, steps.getPathId())
                .eq(StepRelation::getStepId, steps.getId()));
        if (ObjectUtils.isNull(relations) || CollUtils.isEmpty(relations)) {
            vo.setCourses(new ArrayList<>(0));
            return vo;
        }
        List<RelationCourseForm> list = getComplexList(relations);
        vo.setCourses(list);
        return vo;
    }

    /**
     * 查询复杂列表
     * @param relations
     * @return
     */
    private List<RelationCourseForm> getComplexList(List<StepRelation> relations) {

        List<RelationCourseForm> list = new ArrayList<>();
        // 遍历关联课程，将对应的课程查找出来
        for (StepRelation item : relations) {
            RelationCourseForm form = new RelationCourseForm();
            if (item.getRelationType().equals(REPLAY_COURSE)) {
                CourseSimpleVO course = courseFeignClient.getCourseSimpleById(item.getRelationId()).getData();
                form.setCover(course.getCoverLink());
                form.setName(course.getTitle());
                form.setPrice(String.valueOf(course.getPrice()));
                form.setRelationId(course.getId());
                form.setTypeText("录播课");
                list.add(form);
            } else if (item.getRelationType().equals(LIVE_COURSE)) {
                LiveCourseSimpleVO course = courseFeignClient.getLiveCourseSimpleById(item.getRelationId()).getData();
                form.setCover(course.getCover());
                form.setName(course.getTitle());
                form.setPrice(String.valueOf(course.getPrice()));
                form.setRelationId(course.getId());
                form.setTypeText("直播课");
                list.add(form);
            } else if (item.getRelationType().equals(IMAGE_TEXT)) {
                ImageTextSimpleVO course = bookFeignClient.getSimpleImageText(item.getRelationId()).getData();
                form.setCover(course.getCoverLink());
                form.setName(course.getTitle());
                form.setPrice(String.valueOf(course.getPrice()));
                form.setRelationId(course.getId());
                form.setTypeText("图文");
                list.add(form);
            } else if (item.getRelationType().equals(E_BOOK)) {
                EBookSimpleVO course = bookFeignClient.getEBookSimpleById(item.getRelationId()).getData();
                form.setCover(course.getCoverLink());
                form.setName(course.getName());
                form.setPrice(String.valueOf(course.getPrice()));
                form.setRelationId(course.getId());
                form.setTypeText("电子书");
                list.add(form);
            }
        }
        return list;
    }

    @Override
    public Boolean updateStep(Integer stepId, StepForm form) {
        Steps steps = getById(stepId);
        if (ObjectUtils.isNull(steps)) {
            return false;
        }
        steps.setName(form.getName());
        steps.setSort(form.getSort());
        steps.setIntro(form.getIntro());
        steps.setCourseCount(ObjectUtils.isNull(form.getCourses()) ? 0 : form.getCourses().size());
        // 更新关联库
        Boolean ok = relationService.updateRelation(stepId, form.getPathId(), form.getCourses());
        return ok && updateById(steps);
    }

    @Override
    public Boolean addStep(StepForm form) {
        Steps steps = new Steps();
        steps.setIntro(form.getIntro());
        steps.setName(form.getName());
        steps.setSort(form.getSort());
        steps.setPathId(form.getPathId());
        steps.setCourseCount(ObjectUtils.isNull(form.getCourses()) ? 0 : form.getCourses().size());
        save(steps);
        Steps one = getOne(new LambdaQueryWrapper<Steps>()
                .eq(Steps::getName, form.getName())
                .eq(Steps::getPathId, form.getPathId())
                .last(" limit 1"));
        // 新增关联库
        relationService.addRelation(one.getId(), form.getPathId(), form.getCourses());
        return true;
    }

    @Override
    public List<StepForm> getStepFullList(Integer pathId) {
        List<StepVO> vos = getStepList(pathId);
        if (ObjectUtils.isNull(vos) || CollUtils.isEmpty(vos)) {
            return new ArrayList<>(0);
        }
        return vos.stream().map(item -> {
            StepForm form = new StepForm();
            BeanUtils.copyProperties(item, form);
            // 查询步骤关联的课程
            List<StepRelation> relations = relationService.list(new LambdaQueryWrapper<StepRelation>()
                    .eq(StepRelation::getPathId, item.getPathId())
                    .eq(StepRelation::getStepId, item.getId()));
            if (ObjectUtils.isNotNull(relations) || CollUtils.isNotEmpty(relations)) {
                List<RelationCourseForm> list = getComplexList(relations);
                form.setCourses(list);
            }
            return form;
        }).collect(Collectors.toList());
    }
}
