package com.zch.gateway.controller;

import com.zch.common.domain.result.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Poison02
 * @date 2024/1/12
 */
@RestController
@RequestMapping("/api/system")
public class SystemController {

    @GetMapping("/config")
    public Response getConfig() {
        Map<String, Object> result = new HashMap<>();
        Map<String, String> video = new HashMap<>();
        Map<String, String> url = new HashMap<>();
        Map<String, Object> system = new HashMap<>();
        url.put("api", "https://demo-api.meedu.xyz");
        url.put("pc", "https://demo-api.meedu.xyz");
        url.put("h5", "https://demo-api.meedu.xyz");
        video.put("default_service", "tencent");
        system.put("version", "V1.0.0");
        system.put("logo", "https://meedu-cos.meedu.xyz/images/FgY3kh3IdDhrJfb5NGueOVgol2WWeKRtwFP4lYUz.png");
        system.put("url", url);
        result.put("video", video);
        result.put("system", system);
        return Response.success(result);
    }

}
