package com.zch.exam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.vo.exam.LevelsVO;
import com.zch.exam.domain.po.Levels;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/2/25
 */
public interface ILevelsService extends IService<Levels> {

    List<LevelsVO> getLevelsList();

    LevelsVO getLevelsById(Integer id);

}
