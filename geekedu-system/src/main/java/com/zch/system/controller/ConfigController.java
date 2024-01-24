package com.zch.system.controller;

import com.zch.common.mvc.result.Response;
import com.zch.system.service.IAddonsService;
import com.zch.system.service.IPcConfigService;
import com.zch.system.service.IVersionInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    private final IPcConfigService pcConfigService;

    @GetMapping("/v2/config")
    public Response getV2Config() {
        return Response.success(pcConfigService.getPcConfig());
    }

    @GetMapping("/v2/navs")
    public Response getV2Navs() {
        return Response.success(pcConfigService.getNavs());
    }

    @GetMapping("/v2/links")
    public Response getV2Links() {
        return Response.success(pcConfigService.getLinks());
    }

    @GetMapping("/v2/sliders")
    public Response getSliders(@RequestParam("platform") String platform) {
        return Response.success(pcConfigService.getSliders(platform));
    }

    @GetMapping("/v2/announcement/{id}")
    public Response getOneAnnouncement(@RequestParam(value = "id", required = false) Integer id) {
        return Response.success(pcConfigService.getOneNotice(id));
    }

    @GetMapping("/v2/announcement")
    public Response getAnnouncement() {
        return Response.success(pcConfigService.getAllNotice());
    }

    @GetMapping("/v2/announcement/latest")
    public Response getLatestAnnouncement() {
        return Response.success(pcConfigService.getLatestNotice());
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

    @GetMapping("/dashboard")
    public Response getDashboard() {
        return Response.success(versionInfoService.getDashboard());
    }

    @GetMapping("/dashboard/graph")
    public Response getGraph(@RequestParam("start_at") String startAt,
                             @RequestParam("end_at") String endAt) {
        return Response.success();
    }

}
