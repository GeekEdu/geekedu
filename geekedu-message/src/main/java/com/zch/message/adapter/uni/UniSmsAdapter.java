package com.zch.message.adapter.uni;

import com.apistd.uni.UniException;
import com.apistd.uni.UniResponse;
import com.apistd.uni.sms.UniMessage;
import com.apistd.uni.sms.UniSMS;
import com.zch.message.adapter.SmsAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Poison02
 * @date 2024/3/6
 */
public class UniSmsAdapter implements SmsAdapter {
    @Override
    public void send(String code, String phone, String signature, String template) {
        // 设置自定义参数 (变量短信)
        Map<String, String> templateData = new HashMap<>();
        templateData.put("code", code);

        // 构建信息
        UniMessage message = UniSMS.buildMessage()
                .setTo(phone)
                .setSignature(signature)
                .setTemplateId(template)
                .setTemplateData(templateData);

        // 发送短信
        try {
            UniResponse res = message.send();
            System.out.println(res);
        } catch (UniException e) {
            System.out.println("Error: " + e);
            System.out.println("RequestId: " + e.requestId);
        }
    }
}
