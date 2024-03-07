package com.zch.message.controller;

import com.zch.common.mvc.result.Response;
import com.zch.message.adapter.SmsAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Poison02
 * @date 2024/3/7
 */
@RestController
@RequestMapping("/api/sms")
@RequiredArgsConstructor
public class SmsController {

    private final SmsAdapter smsAdapter;

    @GetMapping("/uni/send")
    public Response sendUniSms() {
        smsAdapter.send("666888", "60", "18040395240", "poison的学习", "pub_verif_basic");
        return Response.success("Ok");
    }

}
