package com.zch.system.controller;

import com.zch.api.vo.system.notice.NoticeVO;
import com.zch.common.mvc.result.Response;
import com.zch.system.service.IAddonsService;
import com.zch.system.service.INoticeService;
import com.zch.system.service.IPcConfigService;
import com.zch.system.service.IVersionInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    private final INoticeService noticeService;

    /**
     * 前台获取配置信息
     * @return
     */
    @GetMapping("/v2/config")
    public Response getV2Config() {
        return Response.success(pcConfigService.getPcConfig());
    }

    /**
     * 前台获取导航栏信息
     * @return
     */
    @GetMapping("/v2/navs")
    public Response getV2Navs() {
        return Response.success(pcConfigService.getNavs());
    }

    /**
     * 前台获取链接 底部友情链接
     * @return
     */
    @GetMapping("/v2/links")
    public Response getV2Links() {
        return Response.success(pcConfigService.getLinks());
    }

    /**
     * 前台获取走马灯 轮播图
     * @param platform
     * @return
     */
    @GetMapping("/v2/sliders")
    public Response getSliders(@RequestParam("platform") String platform) {
        return Response.success(pcConfigService.getSliders(platform));
    }

    /**
     * 根据id查看公告明细
     * @param id
     * @return
     */
    @GetMapping("/v2/announcement/{id}")
    public Response<NoticeVO> getOneAnnouncement(@RequestParam(value = "id", required = false) Integer id) {
        return Response.success(noticeService.getNoticeById(id));
    }

    /**
     * 获取公告列表
     * @return
     */
    @GetMapping("/v2/announcement")
    public Response<List<NoticeVO>> getAnnouncement() {
        return Response.success(noticeService.getNoticeList());
    }

    /**
     * 获取最新公告
     * @return
     */
    @GetMapping("/v2/announcement/latest")
    public Response<NoticeVO> getLatestAnnouncement() {
        return Response.success(noticeService.getLatestNotice());
    }

    /**
     * 前台获取 问答配置
     * @return
     */
    @GetMapping("/v2/ask/config")
    public Response getAskConfigInfo() {
        return Response.success(pcConfigService.getAskConfig());
    }

    /**
     * 前台 获取电子书推荐
     * @return
     */
    @GetMapping("/v2/eBook/recommend")
    public Response getEBookRecommendConfig() {
        return Response.success();
    }
///////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 后台获取配置信息
     * @return
     */
    @GetMapping("/config")
    public Response getConfig() {
        return Response.success(versionInfoService.getConfig());
    }

    /**
     * 前台获取版本信息
     * @return
     */
    @GetMapping("/version/info")
    public Response getVersionInfo() {
        return Response.success(versionInfoService.getInfo());
    }

    /**
     * 后台获取权限信息
     * @return
     */
    @GetMapping("/addons")
    public Response getAddons() {
        return Response.success(addonsService.getAddons());
    }

    /**
     * 后台看板信息
     * @return
     */
    @GetMapping("/dashboard")
    public Response getDashboard() {
        return Response.success(versionInfoService.getDashboard());
    }

    /**
     * 后台统计图信息
     * @param startAt
     * @param endAt
     * @return
     */
    @GetMapping("/dashboard/graph")
    public Response getGraph(@RequestParam("start_at") String startAt,
                             @RequestParam("end_at") String endAt) {
        return Response.success(pcConfigService.getGraph(startAt, endAt));
    }

}
