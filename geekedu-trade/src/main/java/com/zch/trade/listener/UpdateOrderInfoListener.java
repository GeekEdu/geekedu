package com.zch.trade.listener;

import com.zch.trade.service.IOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author Poison02
 * @date 2024/3/21
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = "orderInfo-update-topic", consumerGroup = "trade-service-orderInfo-update-topic")
@RequiredArgsConstructor
public class UpdateOrderInfoListener implements RocketMQListener<String> {

    private final IOrderService orderService;

    @Override
    public void onMessage(String orderNumber) {
        orderService.updateOrderStatus(orderNumber);
    }
}
