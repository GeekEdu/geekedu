package com.zch.common.core.utils.sms;

import com.zch.common.sms.adapter.SmsAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Poison02
 * @date 2024/3/31
 */
@Component
@RequiredArgsConstructor
public class SmsUtils {

    private final SmsAdapter smsAdapter;

    public void send(String code, String ttl, String phone, String signature, String template) {
        smsAdapter.send(code, ttl, phone, signature, template);
    }

}
