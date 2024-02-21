package com.zch;

import com.zch.common.core.listener.AppStartupListener;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

/**
 * @author Poison02
 * @date 2024/1/27
 */
@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
@MapperScan("com.zch.ask.mapper")
public class AskApplication {
    public static void main(String[] args) {
        SpringApplication.run(AskApplication.class, args);
    }

    @Bean
    public AppStartupListener appStartupListener(){
        return new AppStartupListener();
    }

}
