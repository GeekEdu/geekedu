package com.zch.exam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.vo.exam.LevelsVO;
import com.zch.common.core.utils.BeanUtils;
import com.zch.common.core.utils.CollUtils;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.exam.domain.po.Levels;
import com.zch.exam.mapper.LevelsMapper;
import com.zch.exam.service.ILevelsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Poison02
 * @date 2024/2/25
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class LevelsServiceImpl extends ServiceImpl<LevelsMapper, Levels> implements ILevelsService {

    private final LevelsMapper levelsMapper;

    @Override
    public List<LevelsVO> getLevelsList() {
        List<Levels> levels = levelsMapper.selectList(new LambdaQueryWrapper<Levels>());
        if (ObjectUtils.isNotNull(levels) || CollUtils.isEmpty(levels)) {
            return new ArrayList<>(0);
        }
        List<LevelsVO> vos = levels.stream().map(item -> {
            LevelsVO vo = new LevelsVO();
            BeanUtils.copyProperties(item, vo);
            return vo;
        }).collect(Collectors.toList());
        return vos;
    }

    @Override
    public LevelsVO getLevelsById(Integer id) {
        Levels levels = levelsMapper.selectOne(new LambdaQueryWrapper<Levels>()
                .eq(Levels::getId, id));
        if (ObjectUtils.isNull(levels)) {
            return null;
        }
        LevelsVO vo = new LevelsVO();
        BeanUtils.copyProperties(levels, vo);
        return vo;
    }
}
