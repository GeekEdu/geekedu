package com.zch.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.system.domain.po.*;
import com.zch.system.domain.vo.NoticeVO;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/1/24
 */
public interface IPcConfigService extends IService<PcConfig> {

    PcConfig getPcConfig();

    List<Navs> getNavs();

    List<Links> getLinks();

    List<Sliders> getSliders(String platform);

    NoticeVO getAllNotice();

    Notice getOneNotice(Integer id);

    Notice getLatestNotice();

}
