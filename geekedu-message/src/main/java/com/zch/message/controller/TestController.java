package com.zch.message.controller;

import com.zch.message.domain.User;
import com.zch.message.listener.MQProducerService;
import org.apache.rocketmq.client.producer.SendResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Poison02
 * @date 2024/3/8
 */
@RestController
@RequestMapping("/api")
public class TestController {

//    @Resource
//    private MQProducerService mqProducerService;

//    @GetMapping("/send")
//    public void send() {
//        User user = new User();
//        user.setName("nameIsOk");
//        user.setAge(100);
//        mqProducerService.send(user);
//    }
//
//    @GetMapping("/sendTag")
//    public SendResult sendTag() {
//        SendResult sendResult = mqProducerService.sendTagMsg("带有tag的字符消息");
//        return sendResult;
//    }

}
