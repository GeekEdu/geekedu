package com.zch.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.vo.system.DashboardVO;
import com.zch.system.domain.po.VersionInfo;
import com.zch.api.vo.system.ConfigVO;
import com.zch.api.vo.system.VersionInfoVO;

/**
 * @author Poison02
 * @date 2024/1/15
 */
public interface IVersionInfoService extends IService<VersionInfo> {

    /**
     * 获取配置信息
     * @return
     */
    ConfigVO getConfig();

    /**
     * 获取版本信息
     * @return
     */
    VersionInfoVO getInfo();


    DashboardVO getDashboard();

}
