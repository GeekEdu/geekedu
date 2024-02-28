package com.zch.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.vo.user.VipVO;
import com.zch.common.core.utils.BeanUtils;
import com.zch.common.core.utils.CollUtils;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.user.domain.po.VipInfo;
import com.zch.user.mapper.VipInfoMapper;
import com.zch.user.service.IVipInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Poison02
 * @date 2024/2/28
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class VipInfoServiceImpl extends ServiceImpl<VipInfoMapper, VipInfo> implements IVipInfoService {

    private final VipInfoMapper vipInfoMapper;

    @Override
    public List<VipVO> getVipList() {
        List<VipInfo> list = vipInfoMapper.selectList(new LambdaQueryWrapper<VipInfo>());
        if (ObjectUtils.isNull(list) || CollUtils.isEmpty(list)) {
            return new ArrayList<>(0);
        }
        List<VipVO> vos = list.stream().map(item -> {
            VipVO vo = new VipVO();
            BeanUtils.copyProperties(item, vo);
            return vo;
        }).collect(Collectors.toList());
        return vos;
    }

    @Override
    public VipVO getVipById(Integer id) {
        if (ObjectUtils.isNull(id)) {
            return null;
        }
        VipInfo vipInfo = vipInfoMapper.selectById(id);
        if (ObjectUtils.isNull(vipInfo)) {
            return null;
        }
        VipVO vo = new VipVO();
        BeanUtils.copyProperties(vipInfo, vo);
        return vo;
    }
}
