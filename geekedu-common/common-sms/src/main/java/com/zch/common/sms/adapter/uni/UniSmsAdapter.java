package com.zch.common.sms.adapter.uni;

import com.apistd.uni.UniException;
import com.apistd.uni.UniResponse;
import com.apistd.uni.sms.UniMessage;
import com.apistd.uni.sms.UniSMS;
import com.zch.common.sms.adapter.SmsAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Poison02
 * @date 2024/3/6
 */
@Component
public class UniSmsAdapter implements SmsAdapter {

    @Value("${sms.signature}")
    private String SIGNATURE;

    @Value("${sms.template}")
    private String TEMPLATE;

    @Override
    public void send(String code, String ttl, String phone, String signature, String template) {
        // 设置自定义参数 (变量短信)
        Map<String, String> templateData = new HashMap<>();
        templateData.put("code", code);
        templateData.put("ttl", ttl);

        // 构建信息
        UniMessage message = UniSMS.buildMessage()
                .setTo(phone)
                .setSignature(("".equals(signature) || signature == null) ? SIGNATURE : signature)
                .setTemplateId(("".equals(template) || template == null) ? TEMPLATE : template)
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
