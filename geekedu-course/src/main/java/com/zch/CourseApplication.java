package com.zch;

import com.zch.common.listener.AppStartupListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author Poison02
 * @date 2024/1/9
 */
@SpringBootApplication
public class CourseApplication {
    public static void main(String[] args) {
        SpringApplication.run(CourseApplication.class, args);
    }

    @Bean
    public AppStartupListener appStartupListener(){
        return new AppStartupListener();
    }

}
