package com.zch.trade.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.dto.trade.seckill.SecKillForm;
import com.zch.api.vo.trade.seckill.SecondKillVO;
import com.zch.trade.domain.po.SecondKill;

/**
 * @author Poison02
 * @date 2024/3/25
 */
public interface ISecondKillService extends IService<SecondKill> {

    /**
     * 查询秒杀列表
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param keywords
     * @return
     */
    Page<SecondKillVO> querySecKillList(Integer pageNum, Integer pageSize, String sort, String order, String keywords);

    /**
     * 查询秒杀详情
     * @param id
     * @return
     */
    SecondKillVO querySecKillDetail(Integer id);

    /**
     * 添加秒杀
     * @param form
     * @return
     */
    Boolean addSecKill(SecKillForm form);

    /**
     * 更新秒杀
     * @param id
     * @param form
     * @return
     */
    Boolean updateSecKill(Integer id, SecKillForm form);

    /**
     * 删除秒杀
     * @param id
     * @return
     */
    Boolean deleteSecKill(Integer id);

    //=========================================================================
    /**
     * 前台秒杀详情
     * @param goodsId
     * @param goodsType
     * @return
     */
    SecondKillVO getV2Detail(Integer goodsId, String goodsType);

}
