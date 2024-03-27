package com.zch.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.vo.system.GraphVO;
import com.zch.api.vo.system.ask.AskDiyConfigVO;
import com.zch.system.domain.po.Links;
import com.zch.system.domain.po.Navs;
import com.zch.system.domain.po.PcConfig;
import com.zch.system.domain.po.Sliders;

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

    /**
     * 图表统计
     * @return
     */
    GraphVO getGraph();

    /**
     * 前台 获取问答自定义配置
     * @return
     */
    AskDiyConfigVO getAskConfig();

}
