package com.zch.trade.listener;

import com.zch.api.dto.trade.pay.PayInfoForm;
import com.zch.trade.service.IPayInfoService;
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
@RocketMQMessageListener(topic = "payInfo-2Mysql-topic", consumerGroup = "trade-service-payInfo-2Mysql-topic")
@RequiredArgsConstructor
public class WritePayInfo2MySQLListener implements RocketMQListener<PayInfoForm> {

    private final IPayInfoService payInfoService;

    @Override
    public void onMessage(PayInfoForm form) {
        payInfoService.createPayInfo(form);
    }
}
