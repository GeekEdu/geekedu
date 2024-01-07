package com.zch;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Poison02
 * @date 2024/1/7
 */
@SpringBootApplication
@MapperScan({"com.zch.label.mapper"})
public class LabelApplication {
    public static void main(String[] args) {
        SpringApplication.run(LabelApplication.class, args);
    }
}
