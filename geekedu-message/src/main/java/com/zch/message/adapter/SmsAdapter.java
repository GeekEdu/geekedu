package com.zch.message.adapter;

/**
 * SMS 适配器
 * @author Poison02
 * @date 2024/3/6
 */
public interface SmsAdapter {

    /**
     * 发送短信
     * @param code 如 验证码
     * @param phone 手机号
     * @param signature 短信签名
     * @param template 短信模板
     */
    void send(String code, String phone, String signature, String template);

}
