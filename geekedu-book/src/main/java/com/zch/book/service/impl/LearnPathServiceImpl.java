package com.zch.book.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.dto.path.LearnPathForm;
import com.zch.api.dto.path.StepForm;
import com.zch.api.feignClient.label.LabelFeignClient;
import com.zch.api.vo.path.LearnPathVO;
import com.zch.api.vo.path.StepEndVO;
import com.zch.api.vo.path.StepVO;
import com.zch.book.domain.po.LearnPath;
import com.zch.book.mapper.LearnPathMapper;
import com.zch.book.service.ILearnPathService;
import com.zch.book.service.IStepsService;
import com.zch.common.core.utils.BeanUtils;
import com.zch.common.core.utils.CollUtils;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.common.core.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Poison02
 * @date 2024/3/13
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class LearnPathServiceImpl extends ServiceImpl<LearnPathMapper, LearnPath> implements ILearnPathService {

    private final LabelFeignClient labelFeignClient;

    private final IStepsService stepsService;

    @Override
    public Page<LearnPathVO> getPathList(Integer pageNum, Integer pageSize, String keywords, Integer categoryId) {
        Page<LearnPathVO> vo = new Page<>();
        LambdaQueryWrapper<LearnPath> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(keywords)) {
            wrapper.like(LearnPath::getName, keywords);
        }
        if (ObjectUtils.isNotNull(categoryId)) {
            wrapper.eq(LearnPath::getCategoryId, categoryId);
        }
        Page<LearnPath> page = page(new Page<LearnPath>(pageNum, pageSize), wrapper);
        if (ObjectUtils.isNull(page) || ObjectUtils.isNull(page.getRecords()) || CollUtils.isEmpty(page.getRecords())) {
            vo.setTotal(0);
            vo.setRecords(new ArrayList<>(0));
        }
        vo.setRecords(page.getRecords().stream().map(item -> {
            LearnPathVO vo1 = new LearnPathVO();
            BeanUtils.copyProperties(item, vo1);
            vo1.setCategory(labelFeignClient.getCategoryById(item.getCategoryId(), "LEARN_PATH").getData());
            return vo1;
        }).collect(Collectors.toList()));
        vo.setTotal(page.getTotal());
        return vo;
    }

    @Override
    public LearnPathVO getPathDetail(Integer id) {
        LearnPathVO vo = new LearnPathVO();
        if (ObjectUtils.isNull(id)) {
            return vo;
        }
        LearnPath path = getById(id);
        if (ObjectUtils.isNotNull(path)) {
            BeanUtils.copyProperties(path, vo);
            vo.setCategory(labelFeignClient.getCategoryById(path.getCategoryId(), "LEARN_PATH").getData());
        }
        return vo;
    }

    @Override
    public Boolean deletePath(Integer id) {
        return removeById(id);
    }

    @Override
    public Boolean updatePath(Integer id, LearnPathForm form) {
        if (ObjectUtils.isNull(id)) {
            return false;
        }
        LearnPath path = getById(id);
        if (ObjectUtils.isNotNull(path)) {
            path.setName(form.getName());
            path.setCover(form.getCover());
            path.setCategoryId(form.getCategoryId());
            path.setOriginPrice(StringUtils.isBlank(form.getOriginPrice()) ? BigDecimal.ZERO : new BigDecimal(form.getOriginPrice()));
            path.setPrice(StringUtils.isBlank(form.getPrice()) ? BigDecimal.ZERO : new BigDecimal(form.getPrice()));
            path.setIsShow(form.getIsShow());
            path.setIntro(form.getIntro());
            path.setGroundingTime(form.getGroundingTime());
            return true;
        }
        return false;
    }

    @Override
    public Boolean addPath(LearnPathForm form) {
        LearnPath path = new LearnPath();
        path.setName(form.getName());
        path.setCover(form.getCover());
        path.setCategoryId(form.getCategoryId());
        path.setOriginPrice(StringUtils.isBlank(form.getOriginPrice()) ? BigDecimal.ZERO : new BigDecimal(form.getOriginPrice()));
        path.setPrice(StringUtils.isBlank(form.getPrice()) ? BigDecimal.ZERO : new BigDecimal(form.getPrice()));
        path.setIsShow(form.getIsShow());
        path.setIntro(form.getIntro());
        path.setGroundingTime(form.getGroundingTime());
        return save(path);
    }

    @Override
    public List<StepVO> getStepList(Integer pathId) {
        return stepsService.getStepList(pathId);
    }

    @Override
    public Boolean deleteStepById(Integer stepId) {
        if (ObjectUtils.isNull(stepId)) {
            return false;
        }
        return stepsService.deleteStepById(stepId);
    }

    @Override
    public StepEndVO getStepDetail(Integer id) {
        return stepsService.getStepDetail(id);
    }

    @Override
    public Boolean updateStep(Integer stepId, StepForm form) {
        return stepsService.updateStep(stepId, form);
    }

    @Override
    public Boolean addStep(StepForm form) {
        return stepsService.addStep(form);
    }
}