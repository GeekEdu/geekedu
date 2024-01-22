package com.zch.system.controller;

import com.zch.api.feignClient.user.UserFeignClient;
import com.zch.common.mvc.result.Response;
import com.zch.system.service.IAddonsService;
import com.zch.system.service.IVersionInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * @author Poison02
 * @date 2024/1/14
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ConfigController {

    private final IVersionInfoService versionInfoService;

    private final IAddonsService addonsService;

    private final UserFeignClient userFeignClient;

    @GetMapping("/config")
    public Response getConfig() {
        return Response.success(versionInfoService.getConfig());
    }

    @GetMapping("/version/info")
    public Response getVersionInfo() {
        return Response.success(versionInfoService.getInfo());
    }

    @GetMapping("/addons")
    public Response getAddons() {
        return Response.success(addonsService.getAddons());
    }

    @GetMapping("/dashboard")
    public Response getDashboard() {
        return Response.success(versionInfoService.getDashboard());
    }

    @GetMapping("/dashboard/graph")
    public Response getGraph(@RequestParam("start_at") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime startAt,
                             @RequestParam("end_at") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime endAt) {
        return Response.success();
    }

}
