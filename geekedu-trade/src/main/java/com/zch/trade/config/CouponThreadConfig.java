package com.zch.trade.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 配置线程池，为了异步生成优惠码
 * @author Poison02
 * @date 2024/4/17
 */
@Configuration
@Slf4j
public class CouponThreadConfig {

    @Bean
    public Executor generateCouponCodeExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心线程池大小
        executor.setCorePoolSize(2);
        // 最大线程池大小
        executor.setMaxPoolSize(5);
        // 队列大小
        executor.setQueueCapacity(200);
        // 线程名称
        executor.setThreadNamePrefix("coupon-code-handler-");
        // 拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    @Bean
    public Executor discountSolutionExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心线程池大小
        executor.setCorePoolSize(12);
        // 最大线程池大小
        executor.setMaxPoolSize(12);
        // 队列大小
        executor.setQueueCapacity(9999);
        // 线程名称
        executor.setThreadNamePrefix("discount-solution-calculator-");
        // 拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        executor.initialize();
        return executor;
    }

}
