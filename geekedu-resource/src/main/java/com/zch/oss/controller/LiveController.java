package com.zch.oss.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.zch.common.mvc.result.Response;
import com.zch.common.satoken.context.UserContext;
import com.zch.oss.service.ILiveService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Poison02
 * @date 2024/3/8
 */
@RestController
@RequestMapping("/api/live")
@RequiredArgsConstructor
public class LiveController {

    private final ILiveService liveService;

    /**
     * 获取推流地址
     * @return
     */
    @GetMapping("/push/url")
    public Response<String> getPushUrl() {
        // 获取当前登录用户id
        Long userId = Long.valueOf((String) StpUtil.getLoginId());
        // Long userId = 1745747394693820416L;
        // 获取当前时间戳，得到一天后的时间戳
        long now = System.currentTimeMillis() / 1000;
        long txTime = now + 24 * 60 * 60;
        return Response.success(liveService.generatePushUrl(userId + "", txTime));
    }

    /**
     * 获取播放地址
     * @return
     */
    @GetMapping("/play/url")
    public Response<String> getPlayUrl() {
        // 获取当前登录用户id
        Long userId = Long.valueOf((String) StpUtil.getLoginId());
        // Long userId = 1745747394693820416L;
        // 获取当前时间戳，得到一天后的时间戳
        long now = System.currentTimeMillis() / 1000;
        long txTime = now + 24 * 60 * 60;
        return Response.success(liveService.generatePlayUrl(userId + "", txTime));
    }

}
