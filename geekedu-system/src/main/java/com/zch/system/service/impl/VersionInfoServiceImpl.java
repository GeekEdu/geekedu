package com.zch.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.vo.system.*;
import com.zch.api.vo.system.System;
import com.zch.system.domain.po.*;
import com.zch.system.mapper.AddonsMapper;
import com.zch.system.mapper.VersionInfoMapper;
import com.zch.system.service.IVersionInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Poison02
 * @date 2024/1/15
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class VersionInfoServiceImpl extends ServiceImpl<VersionInfoMapper, VersionInfo> implements IVersionInfoService {

    private final VersionInfoMapper versionInfoMapper;

    private final AddonsMapper addonsMapper;

    @Override
    public ConfigVO getConfig() {
        Url url = new Url("https://demo-api.meedu.xyz", "https://demo.meedu.xyz", "https://h5.meedu.xyz");
        Video video = new Video("tencent");
        System system = new System("https://meedu-cos.meedu.xyz/images/FgY3kh3IdDhrJfb5NGueOVgol2WWeKRtwFP4lYUz.png", "v1.0.0", url);
        ConfigVO vo = new ConfigVO(system, video);
        return vo;
    }

    @Override
    public VersionInfoVO getInfo() {
        VersionInfo versionInfo = versionInfoMapper.selectOne(new LambdaQueryWrapper<VersionInfo>()
                .eq(VersionInfo::getIsDelete, 0)
                .select(VersionInfo::getSpringbootVersion, VersionInfo::getGeekeduVersion, VersionInfo::getJdkVersion));
        return VersionInfoVO.of(versionInfo.getSpringbootVersion(), versionInfo.getGeekeduVersion(), versionInfo.getJdkVersion());
    }

    @Override
    public DashboardVO getDashboard() {
        DashboardVO vo = new DashboardVO();
        vo.setLastMonthPaidSum(0);
        vo.setThisMonthPaidSum(0);
        vo.setTodayPaidSum(0);
        vo.setTodayPaidUserNum(0);
        vo.setTodayRegisterUserCount(0);
        vo.setUserCount(1);
        vo.setYesterdayPaidSum(0);
        vo.setYesterdayPaidUserNum(0);
        return vo;
    }

}
