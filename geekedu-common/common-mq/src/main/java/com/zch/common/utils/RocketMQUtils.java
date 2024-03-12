package com.zch.common.utils;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.spring.support.RocketMQUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Poison02
 * @date 2024/3/12
 */
public class RocketMQUtils {

    public static void syncSendMsg(DefaultMQProducer producer, Message msg) throws Exception{
        SendResult result = producer.send(msg);
        System.out.println(result);
    }

    public static void asyncSendMsg(DefaultMQProducer producer, Message msg) {
        try {
            producer.send(msg, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    Logger logger = LoggerFactory.getLogger(RocketMQUtil.class);
                    logger.info("异步发送消息成功，消息id：" + sendResult.getMsgId());
                }

                @Override
                public void onException(Throwable e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {

        }
    }
}
