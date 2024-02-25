package com.zch.exam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.vo.exam.TypesVO;
import com.zch.common.core.utils.BeanUtils;
import com.zch.common.core.utils.CollUtils;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.exam.domain.po.Types;
import com.zch.exam.mapper.TypesMapper;
import com.zch.exam.service.ITypesService;
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
public class TypesServiceImpl extends ServiceImpl<TypesMapper, Types> implements ITypesService {

    private final TypesMapper typesMapper;

    @Override
    public List<TypesVO> getTypesList() {
        List<Types> types = typesMapper.selectList(new LambdaQueryWrapper<Types>());
        if (ObjectUtils.isNotNull(types) || CollUtils.isEmpty(types)) {
            return new ArrayList<>(0);
        }
        List<TypesVO> vos = types.stream().map(item -> {
            TypesVO vo = new TypesVO();
            BeanUtils.copyProperties(item, vo);
            return vo;
        }).collect(Collectors.toList());
        return vos;
    }

    @Override
    public TypesVO getTypesById(Integer id) {
        Types types = typesMapper.selectOne(new LambdaQueryWrapper<Types>()
                .eq(Types::getId, id));
        if (ObjectUtils.isNotNull(types)) {
            return null;
        }
        TypesVO vo = new TypesVO();
        BeanUtils.copyProperties(types, vo);
        return vo;
    }
}
