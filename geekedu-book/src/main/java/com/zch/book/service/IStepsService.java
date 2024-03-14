package com.zch.book.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.dto.path.StepForm;
import com.zch.api.vo.path.StepEndVO;
import com.zch.api.vo.path.StepVO;
import com.zch.book.domain.po.Steps;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/3/13
 */
public interface IStepsService extends IService<Steps> {

    /**
     * 获取步骤列表
     * @param pathId
     * @return
     */
    List<StepVO> getStepList(Integer pathId);

    /**
     * 删除步骤
     * @param id
     * @return
     */
    Boolean deleteStepById(Integer id);

    /**
     * 获取步骤详情
     * @return
     */
    StepEndVO getStepDetail(Integer id);

    /**
     * 更新步骤
     * @param stepId
     * @param form
     * @return
     */
    Boolean updateStep(Integer stepId, StepForm form);

    /**
     * 添加步骤
     * @param form
     * @return
     */
    Boolean addStep(StepForm form);

    /**
     * 获取步骤列表
     * @param pathId
     * @return
     */
    List<StepForm> getStepFullList(Integer pathId);

}
