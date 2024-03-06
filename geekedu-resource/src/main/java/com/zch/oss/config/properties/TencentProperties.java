package com.zch.oss.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Poison02
 * @date 2023/12/31
 */
@Data
@ConfigurationProperties(prefix = "geekedu.tencent")
public class TencentProperties {

    private Long appId;

    private String secretId;

    private String secretKey;

    private VodProperties vod;

    private CosProperties cos;

    @Data
    public static class VodProperties{
        /*是否启用腾讯VOD*/
        private boolean enable;

        /*签名有效期*/
        private long vodValidSeconds;

        /*区域*/
        private String region;

        /*任务流*/
        private String procedure;

        /*播放秘钥*/
        private String urlKey;

        private String pKey;

        /*播放器配置*/
        private String pfcg;
    }
    @Data
    public static class CosProperties{
        private boolean enable;
        /*区域*/
        private String region;

        /*存储桶*/
        private String bucket;

        /*触发分块上传的阈值*/
        private long multipartUploadThreshold;

        /*分块上传的最小分块大小*/
        private long minimumUploadPartSize;
    }

}
