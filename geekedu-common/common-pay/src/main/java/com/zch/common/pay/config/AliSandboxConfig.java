package com.zch.common.pay.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 支付宝 沙箱 支付
 * @author Poison02
 * @date 2024/3/7
 */
@Configuration
@ConfigurationProperties(prefix = "alipay")
public class AliSandboxConfig {

    /**
     * App Id
     */
    private String appId;

    /**
     * 私钥
     */
    private String appPrivateKey;

    /**
     * 公钥
     */
    private String alipayPublicKey;

    /**
     * 回调地址
     */
    private String notifyUrl;

    public AliSandboxConfig() {
    }

    public AliSandboxConfig(String appId, String appPrivateKey, String alipayPublicKey, String notifyUrl) {
        this.appId = appId;
        this.appPrivateKey = appPrivateKey;
        this.alipayPublicKey = alipayPublicKey;
        this.notifyUrl = notifyUrl;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppPrivateKey() {
        return appPrivateKey;
    }

    public void setAppPrivateKey(String appPrivateKey) {
        this.appPrivateKey = appPrivateKey;
    }

    public String getAlipayPublicKey() {
        return alipayPublicKey;
    }

    public void setAlipayPublicKey(String alipayPublicKey) {
        this.alipayPublicKey = alipayPublicKey;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }
}
