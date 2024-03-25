package com.zch.trade.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
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

}
