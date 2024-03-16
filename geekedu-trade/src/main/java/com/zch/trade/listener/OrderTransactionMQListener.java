package com.zch.trade.listener;

import com.alibaba.fastjson.JSON;
import com.zch.common.core.utils.StringUtils;
import com.zch.trade.domain.po.Order;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Poison02
 * @date 2024/3/16
 */
@Component
@Slf4j
public class OrderTransactionMQListener implements TransactionListener {

    @Value("${mq.order.confirm.tag.confirm}")
    private String orderConfirmTag;

    @Override
    public LocalTransactionState executeLocalTransaction(Message message, Object o) {
        String tags = message.getTags();
        if (StringUtils.equals(tags, orderConfirmTag)) {
            String body = new String(message.getBody());
            try {
                Order order = JSON.parseObject(body, Order.class);
                // 将订单插入数据库 TODO
                log.info("订单" + order.getOrderId() + " 下单成功");
                // 提交事务
                return LocalTransactionState.COMMIT_MESSAGE;
            } catch (Exception e) {
                e.printStackTrace();
                // 回滚事务
                return LocalTransactionState.ROLLBACK_MESSAGE;
            }
        } else {
            return LocalTransactionState.ROLLBACK_MESSAGE;
        }
    }

    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
        System.out.println("消息Tag: " + messageExt.getTags());
        return LocalTransactionState.COMMIT_MESSAGE;
    }
}
