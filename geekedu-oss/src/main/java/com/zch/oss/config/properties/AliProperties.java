package com.zch.oss.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Poison02
 * @date 2023/12/31
 */
@Data
@ConfigurationProperties(prefix = "geekedu.ali")
public class AliProperties {

    private String accessKeyID;

    private String accessKeySecret;

    private OssProperties oss;

    @Data
    public static class OssProperties {
        private String endPoint;

        private String bucketName;
    }

}
