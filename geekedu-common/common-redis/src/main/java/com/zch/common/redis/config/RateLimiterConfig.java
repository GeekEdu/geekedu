package com.zch.common.redis.config;

import com.zch.common.redis.aspect.RateLimiterAspect;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * @author Poison02
 * @date 2024/3/23
 */
@AutoConfiguration(after = RedissonConfig.class)
public class RateLimiterConfig {

    @Bean
    public RateLimiterAspect rateLimiterAspect() {
        return new RateLimiterAspect();
    }

}
