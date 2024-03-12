package com.zch.trade.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.vo.trade.creditmall.GoodsTypeVO;
import com.zch.trade.domain.po.GoodsType;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/3/12
 */
public interface IGoodsTypeService extends IService<GoodsType> {

    /**
     * 返回所有商品分类列表
     * @return
     */
    List<GoodsTypeVO> getGoodsTypeList();

}
