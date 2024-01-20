package com.zch.system.controller;

import com.zch.api.feignClient.user.UserFeignClient;
import com.zch.api.vo.user.CaptchaVO;
import com.zch.common.domain.result.Response;
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
        Response<CaptchaVO> captcha = userFeignClient.getCaptcha();
        return captcha.getMessage();
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
