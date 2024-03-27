package com.zch.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.feignClient.trade.TradeFeignClient;
import com.zch.api.feignClient.user.UserFeignClient;
import com.zch.api.vo.system.*;
import com.zch.api.vo.system.System;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.common.mvc.result.Response;
import com.zch.system.domain.po.*;
import com.zch.system.mapper.AddonsMapper;
import com.zch.system.mapper.VersionInfoMapper;
import com.zch.system.service.IVersionInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

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

    private final UserFeignClient userFeignClient;

    private final TradeFeignClient tradeFeignClient;

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
        // 查找总学员数
        Response<Long> res1 = userFeignClient.getMemberCount();
        if (ObjectUtils.isNotNull(res1) && ObjectUtils.isNotNull(res1.getData())) {
            vo.setUserCount(res1.getData());
        }
        // 今日注册用户数
        Response<Long> res2 = userFeignClient.todayRegisterCount();
        if (ObjectUtils.isNotNull(res1) && ObjectUtils.isNotNull(res1.getData())) {
            vo.setTodayRegisterUserCount(res2.getData());
        }
        // 上月收入
        Response<BigDecimal> res3 = tradeFeignClient.orderStatCount(1);
        if (ObjectUtils.isNotNull(res3) && ObjectUtils.isNotNull(res3.getData())) {
            vo.setLastMonthPaidSum(res3.getData());
        }
        // 本月收入
        Response<BigDecimal> res4 = tradeFeignClient.orderStatCount(2);
        if (ObjectUtils.isNotNull(res4) && ObjectUtils.isNotNull(res4.getData())) {
            vo.setThisMonthPaidSum(res4.getData());
        }
        // 今日收入
        Response<BigDecimal> res5 = tradeFeignClient.orderStatCount(3);
        if (ObjectUtils.isNotNull(res5) && ObjectUtils.isNotNull(res5.getData())) {
            vo.setTodayPaidSum(res5.getData());
        }
        // 今日支付用户数
        Response<Long> res6 = tradeFeignClient.userStatCount(1);
        if (ObjectUtils.isNotNull(res6) && ObjectUtils.isNotNull(res6.getData())) {
            vo.setTodayPaidUserNum(res6.getData());
        }
        // 昨日支付数
        Response<Long> res7 = tradeFeignClient.userStatCount(2);
        if (ObjectUtils.isNotNull(res7) && ObjectUtils.isNotNull(res7.getData())) {
            vo.setYesterdayPaidSum(res7.getData());
        }
        // 昨日支付用户数
        Response<Long> res8 = tradeFeignClient.userStatCount(3);
        if (ObjectUtils.isNotNull(res8) && ObjectUtils.isNotNull(res8.getData())) {
            vo.setYesterdayPaidUserNum(res8.getData());
        }
        return vo;
    }

}
