package com.zch.oss.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Poison02
 * @date 2023/12/27
 */
@Configuration
public class OSSConfig {

    @Value("${aliun.oss.accessKeyID}")
    private String accessKeyID;

    @Value("${aliun.oss.accessKeySecret}")
    private String accessKeySecret;

    @Value("${aliun.oss.endPoint}")
    private String endPoint;

    @Value("${aliun.oss.bucketName}")
    private String bucketName;

    @Bean
    public OSS ossClient() {
        return new OSSClientBuilder()
                .build(accessKeyID, accessKeySecret, endPoint, bucketName);
    }

}
