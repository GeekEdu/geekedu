package com.zch.trade.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.dto.trade.CreateOrderForm;
import com.zch.api.vo.order.OrderVO;
import com.zch.trade.domain.po.Order;
import com.zch.trade.mapper.OrderMapper;
import com.zch.trade.service.IOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Poison02
 * @date 2024/3/9
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {
    @Override
    public OrderVO createOrder(CreateOrderForm form) {
        return null;
    }
}
