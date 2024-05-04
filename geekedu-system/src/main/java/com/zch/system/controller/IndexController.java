package com.zch.system.controller;

import com.zch.api.vo.system.index.BlockVO;
import com.zch.common.mvc.result.Response;
import com.zch.system.mapper.SlidersMapper;
import com.zch.system.service.IIndexService;
import com.zch.system.vo.MobileIndexVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * 微信小程序首页数据
     * @return
     */
    @GetMapping("/mobile")
    public Response<List<Map<String, Object>>> getMobileIndex() {
        return Response.success(indexData());
    }

    /**
     * 首页数据
     * @return
     */
    private List<Map<String, Object>> indexData() {
        List<Map<String, Object>> data = new ArrayList<>();

        // Search
        Map<String, Object> searchMap = new HashMap<>();
        searchMap.put("type", "search");
        searchMap.put("placeholder", "请输入搜索关键词");
        searchMap.put("checked", false);
        data.add(searchMap);

        // Swiper
        List<Map<String, Object>> swiperData = new ArrayList<>();
        Map<String, Object> swiperItem1 = new HashMap<>();
        swiperItem1.put("src", "https://blog-1315662121.cos.ap-guangzhou.myqcloud.com/photo/a97ca810613a825791f82d79ed5155c9.png");
        swiperItem1.put("url", "#");
        swiperItem1.put("type", "webview");
        swiperItem1.put("course_title", "111111");
        swiperItem1.put("course_id", 538);
        swiperData.add(swiperItem1);

        Map<String, Object> swiperItem2 = new HashMap<>();
        swiperItem2.put("src", "https://blog-1315662121.cos.ap-guangzhou.myqcloud.com/photo/cf2120a1c770fd5e73b9469b749ef07f.png");
        swiperItem2.put("url", "#");
        swiperItem2.put("type", "course");
        swiperItem2.put("course_title", "2222222");
        swiperItem2.put("course_id", 538);
        swiperData.add(swiperItem2);

        Map<String, Object> swiperMap = new HashMap<>();
        swiperMap.put("type", "swiper");
        swiperMap.put("data", swiperData);
        swiperMap.put("checked", false);
        data.add(swiperMap);

        // Icons
        List<Map<String, Object>> iconsData = new ArrayList<>();
        Map<String, Object> icon1 = new HashMap<>();
        icon1.put("src", "http://demo-mp3.oss-cn-shenzhen.aliyuncs.com/egg-edu-demo/e8a7876274d45405e704.png");
        icon1.put("name", "活动");
        icon1.put("url", "#");
        icon1.put("type", "webview");
        icon1.put("course_title", "123321");
        icon1.put("course_id", 538);
        iconsData.add(icon1);

        Map<String, Object> icon2 = new HashMap<>();
        icon2.put("src", "http://demo-mp3.oss-cn-shenzhen.aliyuncs.com/egg-edu-demo/267d76844eefe2e7ce81.png");
        icon2.put("name", "考试");
        icon2.put("url", "");
        icon2.put("type", "module");
        icon2.put("module", "test");
        iconsData.add(icon2);

        Map<String, Object> icon3 = new HashMap<>();
        icon3.put("src", "http://demo-mp3.oss-cn-shenzhen.aliyuncs.com/egg-edu-demo/ea3f0d7724e2119058b8.png");
        icon3.put("name", "秒杀");
        icon3.put("url", "");
        icon3.put("type", "module");
        icon3.put("module", "flashsale");
        iconsData.add(icon3);

        Map<String, Object> icon4 = new HashMap<>();
        icon4.put("src", "http://demo-mp3.oss-cn-shenzhen.aliyuncs.com/egg-edu-demo/e5e8ae6780f437983ec3.png");
        icon4.put("name", "拼团");
        icon4.put("url", "");
        icon4.put("type", "module");
        icon4.put("module", "group");
        iconsData.add(icon4);

        Map<String, Object> icon5 = new HashMap<>();
        icon5.put("src", "http://demo-mp3.oss-cn-shenzhen.aliyuncs.com/egg-edu-demo/1ffe181c57d0b7919824.png");
        icon5.put("name", "直播");
        icon5.put("type", "module");
        icon5.put("url", "");
        icon5.put("module", "live");
        iconsData.add(icon5);

        Map<String, Object> icon6 = new HashMap<>();
        icon6.put("src", "http://demo-mp3.oss-cn-shenzhen.aliyuncs.com/egg-edu-demo/10b103f786cffae72dba.png");
        icon6.put("name", "专栏");
        icon6.put("type", "module");
        icon6.put("url", "");
        icon6.put("module", "column");
        iconsData.add(icon6);

        Map<String, Object> icon7 = new HashMap<>();
        icon7.put("src", "http://demo-mp3.oss-cn-shenzhen.aliyuncs.com/egg-edu-demo/ffc5d0c974d0b1b74abc.png");
        icon7.put("name", "电子书");
        icon7.put("type", "module");
        icon7.put("url", "");
        icon7.put("module", "book");
        iconsData.add(icon7);

        Map<String, Object> icon8 = new HashMap<>();
        icon8.put("src", "http://demo-mp3.oss-cn-shenzhen.aliyuncs.com/egg-edu-demo/f6db135632407f0ff282.png");
        icon8.put("name", "社区");
        icon8.put("type", "module");
        icon8.put("url", "");
        icon8.put("module", "bbs");
        iconsData.add(icon8);

        Map<String, Object> iconsMap = new HashMap<>();
        iconsMap.put("type", "icons");
        iconsMap.put("data", iconsData);
        iconsMap.put("checked", false);
        data.add(iconsMap);

        // Coupon
        Map<String, Object> couponMap = new HashMap<>();
        couponMap.put("type", "coupon");
        couponMap.put("checked", false);
        data.add(couponMap);

        // Promotion
        Map<String, Object> promotionMap = new HashMap<>();
        promotionMap.put("listType", "group");
        promotionMap.put("type", "promotion");
        promotionMap.put("checked", false);
        data.add(promotionMap);

        // course
        List<Map<String, Object>> listData = new ArrayList<>();
        Map<String, Object> listItem1 = new HashMap<>();
        listItem1.put("cover", "https://geekedu-1315662121.cos.ap-chengdu.myqcloud.com/all/6f4b98a849e24799bf812fcc46cf7251.jpg");
        listItem1.put("id", 6);
        listItem1.put("price", "199.00");
        listItem1.put("t_price", "20.00");
        listItem1.put("title", "SpringBoot开发在线教育系统");
        listItem1.put("try", "<p>SpringBoot开发在线教育系统试看内容</p>");
        listItem1.put("type", "media");
        listData.add(listItem1);

        Map<String, Object> listItem2 = new HashMap<>();
        listItem2.put("cover", "https://geekedu-1315662121.cos.ap-chengdu.myqcloud.com/all/958fae5ca65e4f43b38b682d99179448.png");
        listItem2.put("id", 7);
        listItem2.put("price", "199.00");
        listItem2.put("t_price", "20.00");
        listItem2.put("title", "使用React开发在线教育系统");
        listItem2.put("try", "<p>使用React开发在线教育系统试看内容</p>");
        listItem2.put("type", "media");
        listData.add(listItem2);

        Map<String, Object> listItem3 = new HashMap<>();
        listItem3.put("cover", "https://geekedu-1315662121.cos.ap-chengdu.myqcloud.com/all/d102950daa354487b41a257c4bf805a5.png");
        listItem3.put("id", 11);
        listItem3.put("price", "88.00");
        listItem3.put("t_price", "100.00");
        listItem3.put("title", "算法精讲课");
        listItem3.put("try", "<p>算法精讲课</p>");
        listItem3.put("type", "video");
        listData.add(listItem3);

        Map<String, Object> listItem4 = new HashMap<>();
        listItem4.put("cover", "https://geekedu-1315662121.cos.ap-chengdu.myqcloud.com/all/6f4b98a849e24799bf812fcc46cf7251.jpg");
        listItem4.put("id", 3);
        listItem4.put("price", "159.00");
        listItem4.put("t_price", "20.00");
        listItem4.put("title", "ElasticSearch精讲");
        listItem4.put("try", "ElasticSearch精讲");
        listData.add(listItem4);

        Map<String, Object> listItem5 = new HashMap<>();
        listItem5.put("cover", "https://geekedu-1315662121.cos.ap-chengdu.myqcloud.com/all/d102950daa354487b41a257c4bf805a5.png");
        listItem5.put("id", 25);
        listItem5.put("price", "99.00");
        listItem5.put("t_price", "20.00");
        listItem5.put("title", "算法导论");
        listItem5.put("try", "<p>算法导论</p>");
        listItem5.put("type", "audio");
        listData.add(listItem5);

        Map<String, Object> listMap = new HashMap<>();
        listMap.put("type", "list");
        listMap.put("title", "最新课程");
        listMap.put("listType", "one");
        listMap.put("showMore", true);
        Map<String, Object> more = new HashMap<>();
        more.put("title", "演示页面");
        more.put("url", "");
        listMap.put("more", more);
        listMap.put("data", listData);
        listMap.put("checked", true);
        data.add(listMap);

        List<Map<String, Object>> topicData = new ArrayList<>();
        Map<String, Object> topicData1 = new HashMap<>();
        topicData1.put("cover", "https://geekedu-1315662121.cos.ap-chengdu.myqcloud.com/all/99d9c8254e264edfaef5a539e21627a8.png");
        topicData1.put("id", 6);
        topicData1.put("price", "0");
        topicData1.put("t_price", "20.00");
        topicData1.put("title", "人工智能会导致程序员失业吗？");
        topicData1.put("try", "<p>我只能说有可能会，但是还是得看自己的学习能力，总之好好学习吧</p>");
        topicData1.put("type", "topic");

        Map<String, Object> topicData2 = new HashMap<>();
        topicData2.put("cover", "https://geekedu-1315662121.cos.ap-chengdu.myqcloud.com/all/6f4b98a849e24799bf812fcc46cf7251.jpg");
        topicData2.put("id", 6);
        topicData2.put("price", "12");
        topicData2.put("t_price", "20.00");
        topicData2.put("title", "React开发需要会什么技能？");
        topicData2.put("try", "<p>怎么说呢，需要你会前端三大件！</p><p>除此之外，还需要你有很多敲代码的经验。</p>");
        topicData2.put("type", "topic");

        topicData.add(topicData1);
        topicData.add(topicData2);

        Map<String, Object> listMap2 = new HashMap<>();
        listMap2.put("type", "topic");
        listMap2.put("title", "最新图文");
        listMap2.put("listType", "one");
        listMap.put("showMore", true);
        Map<String, Object> topics = new HashMap<>();
        more.put("title", "演示页面");
        more.put("url", "");
        listMap2.put("more", topics);
        listMap2.put("data", topicData);
        listMap2.put("checked", true);
        data.add(listMap2);

        // Image Ad
        Map<String, Object> imageAdMap = new HashMap<>();
        imageAdMap.put("type", "imageAd");
        imageAdMap.put("data", "https://tangzhe123-com.oss-cn-shenzhen.aliyuncs.com/dishaweb/08C9150BC2B163AEC012D6E544C75DD2.png");
        imageAdMap.put("checked", false);
        data.add(imageAdMap);

        return data;
    }

}
