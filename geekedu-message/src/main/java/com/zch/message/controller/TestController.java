package com.zch.message.controller;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Poison02
 * @date 2024/3/8
 */
@RestController
@RequestMapping("/api")
public class TestController {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @RequestMapping("/test")
    public String order() {
        String test = "123测试";
        rocketMQTemplate.convertAndSend("ORDER_TOPIC", test);
        return test;
    }

}
