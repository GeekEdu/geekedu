package com.zch.message.controller;

import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

/**
 * @author Poison02
 * @date 2024/3/8
 */
@Service
@RocketMQMessageListener(topic = "ORDER_TOPIC", consumerGroup = "BREAD_ORDER_GROUP", consumeMode = ConsumeMode.ORDERLY)
public class NormalRocketMqListener implements RocketMQListener<String> {
    @Override
    public void onMessage(String s) {
        System.out.println("普通订阅-接收到的信息：{}"+ s);
    }
}
