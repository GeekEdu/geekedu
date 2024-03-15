package com.zch;

import com.zch.domain.WebSocket;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;

/**
 * @author Poison02
 * @date 2024/3/10
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan("com.zch.mapper")
public class WebsocketApplication {
    public static void main(String[] args) {
        ApplicationContext app = SpringApplication.run(WebsocketApplication.class, args);
        WebSocket.setContext(app);
    }
}
