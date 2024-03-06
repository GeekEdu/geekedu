package com.zch.common.sms.config;

import com.apistd.uni.Uni;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Poison02
 * @date 2024/3/6
 */
@Configuration
public class SmsConfig {

    public static final String ACCESS_KEY_ID = "your access key id";
    private static final String ACCESS_KEY_SECRET = "your access key secret";

    @Bean
    public static void uni() {
        // 简易模式 只需要 ACCESS_KEY
        Uni.init(ACCESS_KEY_ID);
//        Uni.init(ACCESS_KEY_ID, ACCESS_KEY_SECRET);
    }

}
