package com.zch.common.autoConfigure.threadpool;

import com.zch.common.utils.SpringUtils;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executor;

/**
 * 异步配置
 * @author Poison02
 * @date 2024/1/9
 */
@EnableAsync(proxyTargetClass = true)
@Configuration
public class AsyncConfig implements AsyncConfigurer {

    /**
     * 使用系统线程池
     * @return
     */
    @Override
    public Executor getAsyncExecutor() {
        return SpringUtils.getBean("scheduledExecutorService");
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return AsyncConfigurer.super.getAsyncUncaughtExceptionHandler();
    }
}
