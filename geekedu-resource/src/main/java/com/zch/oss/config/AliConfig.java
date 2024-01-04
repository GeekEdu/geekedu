package com.zch.oss.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.zch.oss.adapter.ali.AliFileStorage;
import com.zch.oss.adapter.FileStorageAdapter;
import com.zch.oss.config.properties.AliProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Poison02
 * @date 2023/12/27
 */
@Configuration
@EnableConfigurationProperties(AliProperties.class)
public class AliConfig {

    @Bean
    @ConditionalOnProperty(prefix = "geekedu.file", name = "platform", havingValue = "ALI")
    public OSS aliOssClient(AliProperties properties) {
        return new OSSClientBuilder()
                .build(properties.getOss().getEndPoint(),
                        properties.getAccessKeyID(),
                        properties.getAccessKeySecret());
    }

    @Bean
    @ConditionalOnProperty(prefix = "geekedu.file", name = "platform", havingValue = "ALI")
    public FileStorageAdapter aliFileStorage(OSS aliOssClient, AliProperties properties) {
        return new AliFileStorage(aliOssClient, properties.getOss().getBucketName());
    }

}
