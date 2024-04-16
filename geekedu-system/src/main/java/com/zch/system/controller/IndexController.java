package com.zch.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zch.api.vo.system.index.BlockVO;
import com.zch.common.mvc.result.Response;
import com.zch.system.domain.po.Sliders;
import com.zch.system.mapper.SlidersMapper;
import com.zch.system.service.IIndexService;
import com.zch.system.vo.MobileIndexVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Poison02
 * @date 2024/3/18
 */
@RestController
@RequestMapping("/api/index")
@RequiredArgsConstructor
public class IndexController {

    private final IIndexService indexService;

    private final SlidersMapper slidersMapper;

    /**
     * 获取前台首页布局
     * @return
     */
    @GetMapping("/v2/block")
    public Response<List<BlockVO>> getV2IndexBlock() {
        return Response.success(indexService.getBlockList());
    }

    @GetMapping("/mobile")
    public Response<List<MobileIndexVO>> getMobileIndex() {
        List<MobileIndexVO> vo = new ArrayList<>();
        List<Sliders> sliders = slidersMapper.selectList(new LambdaQueryWrapper<Sliders>()
                .eq(Sliders::getPlatform, "pc")
                .select(Sliders::getThumb, Sliders::getUrl, Sliders::getPlatform, Sliders::getSort,
                        Sliders::getCreatedTime, Sliders::getUpdatedTime, Sliders::getId));
        MobileIndexVO vo1 = new MobileIndexVO();
        vo1.setType("swiper");
        vo1.setData(sliders);
        MobileIndexVO vo2 = new MobileIndexVO();
        vo2.setType("imageAd");
        vo2.setData("https://tangzhe123-com.oss-cn-shenzhen.aliyuncs.com/dishaweb/08C9150BC2B163AEC012D6E544C75DD2.png");
        MobileIndexVO vo3 = new MobileIndexVO();
        vo3.setType("coupon");
        MobileIndexVO vo4 = new MobileIndexVO();
        vo4.setType("promotion");
        vo4.setListType("group");
        MobileIndexVO vo5 = new MobileIndexVO();
        vo5.setType("icons");
        List<String> icons = new ArrayList<>();
        icons.add("{\"src\":\"http://demo-mp3.oss-cn-shenzhen.aliyuncs.com/egg-edu-demo/e8a7876274d45405e704.png\",\"name\":\"活动\",\"url\":\"\",\"type\":\"webview\",\"course_title\":\"VueCli 实战商城后台管理系统\",\"course_id\":538}");
        icons.add("{\"src\":\"http://demo-mp3.oss-cn-shenzhen.aliyuncs.com/egg-edu-demo/267d76844eefe2e7ce81.png\",\"name\":\"考试\",\"url\":\"\",\"type\":\"module\",\"module\":\"test\"}");
        icons.add("{\"src\":\"http://demo-mp3.oss-cn-shenzhen.aliyuncs.com/egg-edu-demo/ea3f0d7724e2119058b8.png\",\"name\":\"秒杀\",\"url\":\"\",\"type\":\"module\",\"module\":\"flashsale\"}");
        icons.add("{\"src\":\"http://demo-mp3.oss-cn-shenzhen.aliyuncs.com/egg-edu-demo/e5e8ae6780f437983ec3.png\",\"name\":\"拼团\",\"url\":\"\",\"type\":\"module\",\"module\":\"group\"}");
        icons.add("{\"src\":\"http://demo-mp3.oss-cn-shenzhen.aliyuncs.com/egg-edu-demo/1ffe181c57d0b7919824.png\",\"name\":\"直播\",\"type\":\"module\",\"url\":\"\",\"page_id\":0,\"page_title\":\"\",\"course_title\":\"\",\"course_id\":\"\",\"module\":\"live\"}");
        icons.add("{\"src\":\"http://demo-mp3.oss-cn-shenzhen.aliyuncs.com/egg-edu-demo/10b103f786cffae72dba.png\",\"name\":\"专栏\",\"type\":\"module\",\"url\":\"\",\"page_id\":0,\"page_title\":\"\",\"course_title\":\"\",\"course_id\":\"\",\"module\":\"column\"}");
        icons.add("{\"src\":\"http://demo-mp3.oss-cn-shenzhen.aliyuncs.com/egg-edu-demo/ffc5d0c974d0b1b74abc.png\",\"name\":\"电子书\",\"type\":\"module\",\"url\":\"\",\"page_id\":0,\"page_title\":\"\",\"course_title\":\"\",\"course_id\":\"\",\"module\":\"book\"}");
        icons.add("{\"src\":\"http://demo-mp3.oss-cn-shenzhen.aliyuncs.com/egg-edu-demo/f6db135632407f0ff282.png\",\"name\":\"社区\",\"type\":\"module\",\"url\":\"\",\"page_id\":0,\"page_title\":\"\",\"course_title\":\"\",\"course_id\":\"\",\"module\":\"bbs\"}");
        vo5.setData(icons);
        MobileIndexVO vo6 = new MobileIndexVO();
        vo6.setType("list");
        vo6.setListType("one");
        List<String> list = new ArrayList<>();
        list.add("{\"cover\":\"http://demo-mp3.oss-cn-shenzhen.aliyuncs.com/egg-edu-demo/10ccf3a973f5193bec3c.png\",\"id\":6,\"price\":\"9.98\",\"t_price\":\"20.00\",\"title\":\"VueCli 实战在线教育后台系统\",\"try\":\"<p>VueCli 实战在线教育后台系统试看内容</p>\",\"type\":\"media\"}");
        list.add("{\"cover\":\"http://demo-mp3.oss-cn-shenzhen.aliyuncs.com/egg-edu-demo/47d1aa930177515cd95e.png\",\"id\":7,\"price\":\"9.98\",\"t_price\":\"20.00\",\"title\":\"uni-app实战视频点播app小程序\",\"try\":\"<p>uni-app实战视频点播app小程序试看内容</p>\",\"type\":\"media\"}");
        list.add("{\"cover\":\"http://demo-mp3.oss-cn-shenzhen.aliyuncs.com/egg-edu-demo/c948f4a7e402473337cb.png\",\"id\":11,\"price\":\"90.00\",\"t_price\":\"100.00\",\"title\":\"uni-app实战直播app全栈开发\",\"try\":\"<p>uni-app实战直播app全栈开发介绍</p>\",\"type\":\"video\"}");
        list.add("{\"cover\":\"http://demo-mp3.oss-cn-shenzhen.aliyuncs.com/egg-edu-demo/2e96031726768d0d493f.png\",\"id\":3,\"price\":\"9.98\",\"t_price\":\"20.00\",\"title\":\"uni-app实战仿微信app开发\",\"try\":\"uni-app实战仿微信app开发描述\"}");
        list.add("{\"cover\":\"http://demo-mp3.oss-cn-shenzhen.aliyuncs.com/egg-edu-demo/b688b9bf339a6ece9f54.png\",\"id\":25,\"price\":\"10.00\",\"t_price\":\"20.00\",\"title\":\"uni-app实战在线教育类app开发\",\"try\":\"<p>uni-app实战在线教育类app开发</p>\",\"type\":\"audio\"}");
        vo6.setData(list);
        vo.add(vo1);
        vo.add(vo2);
        vo.add(vo3);
        vo.add(vo4);
        vo.add(vo5);
        vo.add(vo6);
        return Response.success(vo);
    }

    @GetMapping("/mobile/coupon")
    public Response getCoupons() {
        List<String> data = new ArrayList<>();
        data.add("{\"id\":83,\"type\":\"course\",\"goods_id\":1636,\"price\":\"4.00\",\"c_num\":300000,\"received_num\":361,\"start_time\":\"2022-07-14T16:00:00.000Z\",\"end_time\":\"2025-08-22T16:00:00.000Z\",\"value\":{\"id\":1636,\"title\":\"中级经济法-知识点精讲课\"},\"isgetcoupon\":false}");
        data.add("{\"id\":39,\"type\":\"column\",\"goods_id\":184,\"price\":\"8.90\",\"c_num\":10000,\"received_num\":440,\"start_time\":\"2021-09-09T16:00:00.000Z\",\"end_time\":\"2034-10-16T16:00:00.000Z\",\"value\":{\"id\":184,\"title\":\"VueCli 实战商城后台管理系统\"},\"isgetcoupon\":false}");
        data.add("{\"id\":33,\"type\":\"course\",\"goods_id\":6,\"price\":\"5.00\",\"c_num\":100000,\"received_num\":376,\"start_time\":\"2021-06-28T17:58:01.000Z\",\"end_time\":\"2035-07-28T16:00:00.000Z\",\"value\":{\"id\":6,\"title\":\"VueCli 实战在线教育后台系统\"},\"isgetcoupon\":false}");
        return Response.success(data);
    }

}
