package com.zch.controller;

import com.zch.domain.WebSocket;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Poison02
 * @date 2024/3/10
 */
@RestController
@RequestMapping("/test")
public class WebSocketController {

    @Resource
    private WebSocket webSocket;

    @PostMapping("/jinDuTiao")
    public void jinDuTiao() throws InterruptedException {
        String msg = "";
        int a = 0;
        for (int i = 0; i <= 100; i++) {
            msg = String.valueOf(a);
            Thread.sleep(100);
            webSocket.sendMessage(msg);
            a ++;
        }
    }

}
