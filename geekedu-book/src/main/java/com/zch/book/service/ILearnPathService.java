package com.zch.book.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.dto.path.LearnPathForm;
import com.zch.api.dto.path.StepForm;
import com.zch.api.vo.path.*;
import com.zch.book.domain.po.LearnPath;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/3/13
 */
public interface ILearnPathService extends IService<LearnPath> {

    /**
     * 分页查询路径列表
     * @param pageNum
     * @param pageSize
     * @param keywords
     * @param categoryId
     * @return
     */
    Page<LearnPathVO> getPathList(Integer pageNum, Integer pageSize, String keywords, Integer categoryId);

    /**
     * 根据路径id查询路径详情
     * @param id
     * @return
     */
    LearnPathVO getPathDetail(Integer id);

    /**
     * 根据路径id删除路径
     * @param id
     * @return
     */
    Boolean deletePath(Integer id);

    /**
     * 根据路径id更新路径
     * @param id
     * @param form
     * @return
     */
    Boolean updatePath(Integer id, LearnPathForm form);

    /**
     * 添加路径
     * @param form
     * @return
     */
    Boolean addPath(LearnPathForm form);

    /**
     * 根据路径id查询路径下的步骤
     * @param pathId
     * @return
     */
    List<StepVO> getStepList(Integer pathId);

    /**
     * 删除步骤
     * @param stepId
     * @return
     */
    Boolean deleteStepById(Integer stepId);

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

    //=================================================================
    /**
     * 分页查询路径列表
     * @param pageNum
     * @param pageSize
     * @param categoryId
     * @return
     */
    LearnPathFullVO getV2PathList(Integer pageNum, Integer pageSize, Integer categoryId);

    /**
     * 获取路径详情
     * @param id
     * @return
     */
    LearnPathDetailVO getV2PathDetail(Integer id);

}
