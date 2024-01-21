package com.zch.system.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.zch.api.feignClient.user.UserFeignClient;
import com.zch.common.mvc.result.Response;
import com.zch.common.satoken.context.UserContext;
import com.zch.system.service.IAddonsService;
import com.zch.system.service.IVersionInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/test")
    public String test() {
        System.out.println("是否登录： = " + StpUtil.isLogin());
        return "登录成功！！！" + UserContext.getLoginId();
    }

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

}
