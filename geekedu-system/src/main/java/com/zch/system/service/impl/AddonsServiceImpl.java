package com.zch.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.common.core.utils.BeanUtils;
import com.zch.system.domain.po.Addons;
import com.zch.api.vo.system.AddonsVO;
import com.zch.system.mapper.AddonsMapper;
import com.zch.system.service.IAddonsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Poison02
 * @date 2024/1/16
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AddonsServiceImpl extends ServiceImpl<AddonsMapper, Addons> implements IAddonsService {

    private final AddonsMapper addonsMapper;

    @Override
    public List<AddonsVO> getAddons() {
        List<Addons> addons = addonsMapper.selectList(new LambdaQueryWrapper<Addons>()
                .select(Addons::getName, Addons::getVersion, Addons::getEnabled, Addons::getSign,
                        Addons::getRequired, Addons::getIndexRoute, Addons::getIndexUrl));
        List<AddonsVO> vos = addons.stream().map(item -> {
            AddonsVO vo = new AddonsVO();
            BeanUtils.copyProperties(item, vo);
            return vo;
        }).collect(Collectors.toList());
        vos.forEach(item -> item.setRequired(new ArrayList<>()));
        return vos;
    }
}
