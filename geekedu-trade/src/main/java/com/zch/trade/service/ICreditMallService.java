package com.zch.trade.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.dto.trade.GoodsForm;
import com.zch.api.vo.trade.creditmall.CreditFullVO;
import com.zch.api.vo.trade.creditmall.CreditMallVO;
import com.zch.trade.domain.po.CreditMall;

/**
 * @author Poison02
 * @date 2024/3/12
 */
public interface ICreditMallService extends IService<CreditMall> {

    /**
     * 后台 条件分页查找积分商城列表
     * @param pageNum
     * @param pageSize
     * @param keywords
     * @param type
     * @return
     */
    CreditFullVO getMallListByCondition(Integer pageNum, Integer pageSize, String keywords, String type);

    /**
     * 根据id删除商品
     * @param id
     * @return
     */
    Boolean deleteGoodById(Integer id);

    /**
     * 根据id获取商品明细
     * @param id
     * @return
     */
    CreditMallVO getGoodDetailById(Integer id);

    /**
     * 新增商品
     * @param form
     * @return
     */
    Boolean addGood(GoodsForm form);

    /**
     * 更新商品
     * @param id
     * @param form
     * @return
     */
    Boolean updateGood(Integer id, GoodsForm form);

    /**
     * 前台 分页查找积分商城列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    Page<CreditMallVO> getMallListByPage(Integer pageNum, Integer pageSize);

}
