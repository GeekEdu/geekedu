package com.zch.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.common.mvc.exception.CommonException;
import com.zch.system.domain.po.*;
import com.zch.system.domain.vo.NoticeVO;
import com.zch.system.mapper.*;
import com.zch.system.service.IPcConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                .url("#")
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
    public NoticeVO getAllNotice() {
        List<Notice> notices = noticeMapper.selectList(new LambdaQueryWrapper<Notice>()
                .select(Notice::getId, Notice::getAdminId, Notice::getTitle, Notice::getAnnouncement, Notice::getViewCount,
                        Notice::getUpdatedTime, Notice::getCreatedTime));
        Long total = noticeMapper.selectCount(new LambdaQueryWrapper<Notice>());
        NoticeVO vo = new NoticeVO();
        vo.setData(notices);
        vo.setTotal(total);
        return vo;
    }

    @Override
    public Notice getOneNotice(Integer id) {
        if (ObjectUtils.isNull(id)) {
            throw new CommonException("请传入对应id！");
        }
        return noticeMapper.selectById(id);
    }

    @Override
    public Notice getLatestNotice() {
        return noticeMapper.selectOne(new LambdaQueryWrapper<Notice>()
                .orderByDesc(Notice::getCreatedTime));
    }
}