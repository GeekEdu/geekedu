package com.zch.trade.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.dto.trade.CreateOrderForm;
import com.zch.api.vo.order.OrderVO;
import com.zch.api.vo.trade.order.OrderEndFullVO;
import com.zch.trade.domain.po.Order;

import java.util.List;

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

    OrderEndFullVO getEndOrderList(Integer pageNum, Integer pageSize, String sort, String order,
                                   String orderId, String goodsName, Integer isRefund, Integer status,
                                   List<String> createdTime, String payment);

}
