package com.zch.trade.config;

import com.zch.trade.listener.OrderTransactionMQListener;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Poison02
 * @date 2024/3/16
 */
@Configuration
@EnableAutoConfiguration
public class MQProducerConfiguration {

    @Value("${mq.rocketmq.producer.group}")
    private String producerGroup;

    @Value("${mq.rocketmq.producer.transaction.group}")
    private String producerTransactionGroup;

    @Value("${mq.rocketmq.name-server}")
    private String nameSrcAddr;

    @Value("${mq.rocketmq.producer.send-message-timeout}")
    private Integer sendMsgTimeout;

    @Autowired
    private OrderTransactionMQListener orderTransactionMQListener;

    @Bean
    TransactionMQProducer producer() throws Exception{
        // 1. 创建消息生产者 producer 并制定生产者组名
        TransactionMQProducer producer = new TransactionMQProducer(producerTransactionGroup);
        // 2. 设置 NameServer 地址
        producer.setNamesrvAddr(nameSrcAddr);
        // 设置回调
        producer.setTransactionListener(orderTransactionMQListener);
        // 超时时间设置
        producer.setSendMsgTimeout(sendMsgTimeout);
        // 3. 启动producer
        producer.start();
        System.out.println("========事务生产者启动！===========");
        return producer;
    }

    @Bean
    public DefaultMQProducer getRocketMQProducer() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer(producerGroup);
        producer.setNamesrvAddr(nameSrcAddr);
        producer.setSendMsgTimeout(sendMsgTimeout);
        producer.start();
        System.out.println("=======默认生产者启动！============");
        return producer;
    }

}

