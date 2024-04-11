package com.zch.common.redis.annotation;

import com.zch.common.redis.enums.LimitType;

import java.lang.annotation.*;

/**
 * 限流
 * @author Poison02
 * @date 2024/3/23
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimiter {

    /**
     * 限流key,支持使用Spring el表达式来动态获取方法上的参数值
     * 格式类似于  #code.id #{#code}
     */
    String key() default "";

    /**
     * 限流时间,单位秒
     */
    int time() default 60;

    /**
     * 限流次数
     */
    int count() default 100;

    /**
     * 限流类型
     */
    LimitType limitType() default LimitType.DEFAULT;

    /**
     * 提示消息 支持国际化 格式为 {code}
     */
    String message() default "{rate.limiter.message}";

}