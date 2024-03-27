package com.zch.trade.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zch.trade.domain.dto.OrderCountDTO;
import com.zch.trade.domain.dto.PayCountDTO;
import com.zch.trade.domain.po.Order;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/3/9
 */
public interface OrderMapper extends BaseMapper<Order> {

    /**
     * 创建订单数
     * @return
     */
    List<OrderCountDTO> queryOrderCount();

    /**
     * 支付订单数
     * @return
     */
    List<OrderCountDTO> queryPayCount();

    /**
     * 每日收入
     * @return
     */
    List<PayCountDTO> queryPayMoney();

}
