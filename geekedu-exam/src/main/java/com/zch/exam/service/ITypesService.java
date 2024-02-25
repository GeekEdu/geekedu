package com.zch.exam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.vo.exam.TypesVO;
import com.zch.exam.domain.po.Types;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/2/25
 */
public interface ITypesService extends IService<Types> {

    List<TypesVO> getTypesList();

    TypesVO getTypesById(Integer id);

}
