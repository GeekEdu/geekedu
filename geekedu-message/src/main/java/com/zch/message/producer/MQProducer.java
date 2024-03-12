package com.zch.message.producer;

import com.zch.common.constants.MQConstants;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author Poison02
 * @date 2024/3/12
 */
@Component
public class MQProducer {

    @Value("${rocketmq.name-server}")
    private String nameSrvUrl;

    @Bean("save2MysqlProducer")
    public DefaultMQProducer save2MysqlProducer() {
        DefaultMQProducer producer = new DefaultMQProducer(MQConstants.SAVE_TO_MYSQL_GROUP);
        try {
            producer.setNamesrvAddr(nameSrvUrl);
            producer.start();
            return producer;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Bean("sendSmsProducer")
    public DefaultMQProducer sendSmsProducer() {
        DefaultMQProducer producer = new DefaultMQProducer(MQConstants.USER_LOGIN_GROUP);
        try {
            producer.setNamesrvAddr(nameSrvUrl);
            producer.start();
            return producer;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
