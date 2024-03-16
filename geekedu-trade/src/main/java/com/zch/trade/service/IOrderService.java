package com.zch.trade.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.dto.trade.CreateOrderForm;
import com.zch.api.vo.order.OrderVO;
import com.zch.trade.domain.po.Order;

/**
 * @author Poison02
 * @date 2024/3/9
 */
public interface IOrderService extends IService<Order> {

    /**
     * 创建订单
     * @param form
     * @return
     */
    OrderVO createOrder(CreateOrderForm form);

}
