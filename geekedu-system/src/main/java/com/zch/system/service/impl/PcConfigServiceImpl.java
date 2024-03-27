package com.zch.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.feignClient.trade.TradeFeignClient;
import com.zch.api.feignClient.user.UserFeignClient;
import com.zch.api.vo.system.GraphVO;
import com.zch.api.vo.system.ask.AskDiyConfigVO;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.common.mvc.result.Response;
import com.zch.system.domain.po.*;
import com.zch.system.mapper.*;
import com.zch.system.service.IPcConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author Poison02
 * @date 2024/1/24
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class PcConfigServiceImpl extends ServiceImpl<PcConfigMapper, PcConfig> implements IPcConfigService {

    private final PcConfigMapper pcConfigMapper;

    private final SlidersMapper slidersMapper;

    private final NavsMapper navsMapper;

    private final LinksMapper linksMapper;

    private final NoticeMapper noticeMapper;

    private final TradeFeignClient tradeFeignClient;

    private final UserFeignClient userFeignClient;

    @Override
    public PcConfig getPcConfig() {
        Logo logo = Logo.builder().logo("https://geekedu-1315662121.cos.ap-chengdu.myqcloud.com/logo/logo.png").whiteLogo(null).build();
        BulletSecret bulletSecret = BulletSecret.builder().size("14").color("#CCCCCC").opacity("1").text("${mobile}").build();
        Player player = Player.builder().cover("https://blog-1315662121.cos.ap-guangzhou.myqcloud.com/photo/b74a044751a49b209146c604a517566b.jpg")
                .enabledBulletSecret("1")
                .bulletSecret(bulletSecret).build();
        Member member = Member.builder().enabledFaceVerify(false).enabledMobileBindAlert(0).build();
        Map<String, Integer> socialites = new HashMap<>();
        socialites.put("qq", 1);
        socialites.put("wechat_scan", 1);
        socialites.put("wechat_oauth", 1);
        Map<String, Integer> credit1Reward = new HashMap<>();
        credit1Reward.put("register", 1);
        credit1Reward.put("watched_vod_course", 3);
        credit1Reward.put("watched_video", 4);
        credit1Reward.put("paid_order", 5);
        String[] temp = {"Credit1Mall", "LearningPaths", "GeekeduTopics", "MiaoSha", "Paper", "SinglePage", "TuanGou", "Wenda",
                "GeekeduBooks", "TemplateOne", "Cert", "DaySignIn", "MultiLevelShare", "CodeExchanger", "Zhibo", "WechatJsapiPay", "AliyunHls", "TencentCloudHls"};
        List<String> enabledAddons = new ArrayList<>(List.of(temp));
        // 数据库中查询数据
        List<Sliders> sliders = getSliders("PC");
        List<Navs> navs = getNavs();
        List<Links> links = getLinks();
        PcConfig config = PcConfig.builder()
                .webName("GeekEdu官方网站")
                .name("GeekEdu官方网站")
                .logo(logo)
                .url("https://6968y1r161.yicp.fun")
                .pcUrl("#")
                .wxUrl("#")
                .icp("四川公安备案")
                .icpLink("#")
                .icp2("四川公安备案")
                .icp2Link("#")
                .userProtocol("#")
                .userPrivateProtocol("#")
                .player(player)
                .member(member)
                .socialites(socialites)
                .credit1Reward(credit1Reward)
                .enabledAddons(enabledAddons)
                .sliders(sliders)
                .navs(navs)
                .links(links).build();
        return config;
    }

    @Override
    public List<Navs> getNavs() {
        return navsMapper.selectList(new LambdaQueryWrapper<Navs>()
                .select(Navs::getSort, Navs::getPlatform, Navs::getUrl, Navs::getName, Navs::getBlank,
                        Navs::getParentId, Navs::getId, Navs::getActiveRoutes));
    }

    @Override
    public List<Links> getLinks() {
        return linksMapper.selectList(new LambdaQueryWrapper<Links>()
                .select(Links::getId, Links::getName, Links::getSort, Links::getUrl, Links::getUpdatedTime, Links::getCreatedTime));
    }

    @Override
    public List<Sliders> getSliders(String platform) {
        return slidersMapper.selectList(new LambdaQueryWrapper<Sliders>()
                .eq(Sliders::getPlatform, platform)
                .select(Sliders::getThumb, Sliders::getUrl, Sliders::getPlatform, Sliders::getSort,
                        Sliders::getCreatedTime, Sliders::getUpdatedTime, Sliders::getId));
    }

    @Override
    public GraphVO getGraph() {
        GraphVO vo = new GraphVO();
        Response<Map<LocalDate, Long>> res1 = tradeFeignClient.everyDayOrderCount();
        Response<Map<LocalDate, Long>> res2 = tradeFeignClient.everyDayOrderPay();
        Response<Map<LocalDate, BigDecimal>> res3 = tradeFeignClient.everyDayOrderMoney();
        Response<Map<LocalDate, Long>> res4 = userFeignClient.statRegisterCount();
        if (ObjectUtils.isNotNull(res1) && ObjectUtils.isNotNull(res2.getData())) {
            vo.setOrderCreated(res1.getData());
        }
        if (ObjectUtils.isNotNull(res2) && ObjectUtils.isNotNull(res2.getData())) {
            vo.setOrderPaid(res2.getData());
        }
        if (ObjectUtils.isNotNull(res3) && ObjectUtils.isNotNull(res3.getData())) {
            vo.setOrderSum(res3.getData());
        }
        if (ObjectUtils.isNotNull(res4) && ObjectUtils.isNotNull(res4.getData())) {
            vo.setUserRegister(res4.getData());
        }
        return vo;
    }

    @Override
    public AskDiyConfigVO getAskConfig() {
        AskDiyConfigVO vo = new AskDiyConfigVO();
        vo.setEnableRewardScore(true);
        vo.setDiyContent("<p><b>（该内容后台可</b>自定义<b style=\"font-size: 1em;\">）</b></p><p><b>本站禁止发布以下内容，违者报警并提交相关IP等信息。" +
                "</b></p><p>(一)反对宪法所确定的基本原则的<br/>(二)危害国家安全，泄露国家秘密，颠覆国家政权，破坏国家统一的<br/>(三)损害国家荣誉和利益的<br/>(四)煽动民族仇恨、民族歧视，破坏民族团结的<br/>(五)破坏国家宗教政策，" +
                "宣扬邪教和封建迷信的<br/>(六)散布谣言，扰乱社会秩序，破坏社会稳定的<br/>(七)散布淫秽、色情、赌博、暴力、凶杀、恐怖或者教唆犯罪的<br/>(八)侮辱或者诽谤他人，侵害他人合法权益的<br/>(九)含有法律、行政法规禁止的其他内容的。</p>");
        return vo;
    }

    /**
     * 生成区间日期
     * @param startDate
     * @param endDate
     * @return
     */
    public static Map<String, Integer> generateDate(String startDate, String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDate start = LocalDate.parse(startDate, formatter);
        LocalDate end = LocalDate.parse(endDate, formatter);

        Map<String, Integer> map = new TreeMap<>();
        LocalDate current = start;

        while (!current.isAfter(end)) {
            String formattedDate = current.format(formatter);
            map.put(formattedDate, 0);
            current = current.plusDays(1);
        }

        return map;
    }
}
