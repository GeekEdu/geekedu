package com.zch.system.service;

import com.zch.api.vo.system.index.BlockVO;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/3/18
 */
public interface IIndexService {

    List<BlockVO> getBlockList();

}
