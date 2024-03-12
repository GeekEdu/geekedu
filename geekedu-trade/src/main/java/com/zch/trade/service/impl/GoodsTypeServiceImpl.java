package com.zch.trade.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.vo.trade.creditmall.GoodsTypeVO;
import com.zch.common.core.utils.BeanUtils;
import com.zch.common.core.utils.CollUtils;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.trade.domain.po.GoodsType;
import com.zch.trade.mapper.GoodsTypeMapper;
import com.zch.trade.service.IGoodsTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Poison02
 * @date 2024/3/12
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class GoodsTypeServiceImpl extends ServiceImpl<GoodsTypeMapper, GoodsType> implements IGoodsTypeService {

    private final GoodsTypeMapper goodsTypeMapper;

    @Override
    public List<GoodsTypeVO> getGoodsTypeList() {
        List<GoodsType> types = goodsTypeMapper.selectList(new LambdaQueryWrapper<GoodsType>());
        if (ObjectUtils.isNull(types) || CollUtils.isEmpty(types)) {
            return new ArrayList<>(0);
        }
        return types.stream().map(item -> {
            GoodsTypeVO vo = new GoodsTypeVO();
            BeanUtils.copyProperties(item, vo);
            return vo;
        }).collect(Collectors.toList());
    }
}
