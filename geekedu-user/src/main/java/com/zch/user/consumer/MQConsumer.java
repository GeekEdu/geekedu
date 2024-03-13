//package com.zch.user.consumer;
//
//import com.alibaba.fastjson.JSON;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.zch.common.constants.MQConstants;
//import com.zch.common.core.utils.ObjectUtils;
//import com.zch.user.domain.po.User;
//import com.zch.user.service.IUserService;
//import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
//import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
//import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
//import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
//import org.apache.rocketmq.common.message.MessageExt;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.util.List;
//
///**
// * @author Poison02
// * @date 2024/3/12
// */
//@Component
//public class MQConsumer {
//
////    @Value("${rocketmq.name-server}")
////    private String nameSrvUrl;
//
//    private static final String nameSrvUrl = "8.137.103.164:9876";
//
//    @Resource
//    private IUserService userService;
//
//    @Bean("save2MysqlConsumer")
//    public DefaultMQPushConsumer save2MysqlConsumer() {
//        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(MQConstants.SAVE_TO_MYSQL_GROUP);
//        consumer.setNamesrvAddr(nameSrvUrl);
//        try {
//            consumer.subscribe(MQConstants.SAVE_TO_MYSQL_TOPIC, "*");
//            consumer.registerMessageListener(new MessageListenerConcurrently() {
//                @Override
//                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
//                    MessageExt msg = list.get(0);
//                    if (ObjectUtils.isNull(msg)) {
//                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
//                    }
//                    // 消息体
//                    String bodyStr = new String(msg.getBody());
//                    User user = JSON.parseObject(bodyStr.substring(11, bodyStr.length() - 1)).toJavaObject(User.class);
//                    User toUpdate = new User();
//                    toUpdate.setPoints(user.getPoints());
//                    // 保存数据库
//                    userService.updateById(toUpdate);
//                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
//                }
//            });
//            consumer.start();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return consumer;
//    }
//
//    public static void main(String[] args) {
//        String jsonString = "{\"message\":{\"address\":\"四川省成都市\",\"avatar\":\"https://geekedu-1315662121.cos.ap-chengdu.myqcloud.com/logo/logo.png\",\"client\":\"Chrome\",\"createdBy\":1745747394693820416,\"email\":\"admin@geekedu.com\",\"gender\":false,\"id\":1745747394693820416,\"intro\":\"我是超级管理员\",\"inviteAmount\":0.00,\"inviteCount\":0,\"ipAddress\":\"127.0.0.1\",\"isDelete\":false,\"isFrozen\":false,\"name\":\"超级管理员\",\"password\":\"5eaf78c5a9944922401f1d47c302fd7e\",\"phone\":\"13947382893\",\"points\":7,\"roleId\":1,\"salt\":\"6ed1ok01qdl87uy31e2tejv5\",\"status\":false,\"tagId\":\"1\",\"updatedBy\":1745747394693820416,\"userName\":\"admin@geekedu.com\",\"vipId\":3}}";
//
////        // 使用 Fastjson 将 JSON 字符串转换为 Java 对象
////        User user = JSON.toJavaObject(JSON.parseObject(jsonString), User.class);
//        User user = new User();
//        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            System.out.println(jsonString.substring(11, jsonString.length() - 1));
//            user = objectMapper.readValue(jsonString.substring(11, jsonString.length() - 1), User.class);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        // 打印转换后的对象
//        System.out.println(user);
//    }
//
//}
