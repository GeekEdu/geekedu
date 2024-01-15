package com.zch.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.system.domain.po.VersionInfo;
import com.zch.system.domain.vo.VersionInfoVO;

import java.util.Map;

/**
 * @author Poison02
 * @date 2024/1/15
 */
public interface IVersionInfoService extends IService<VersionInfo> {

    /**
     * 获取配置信息
     * @return
     */
    Map<String, Object> getConfig();

    /**
     * 获取版本信息
     * @return
     */
    VersionInfoVO getInfo();

}
