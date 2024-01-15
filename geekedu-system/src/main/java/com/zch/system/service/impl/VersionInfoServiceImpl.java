package com.zch.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.system.domain.po.VersionInfo;
import com.zch.system.domain.vo.VersionInfoVO;
import com.zch.system.mapper.VersionInfoMapper;
import com.zch.system.service.IVersionInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Poison02
 * @date 2024/1/15
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class VersionInfoServiceImpl extends ServiceImpl<VersionInfoMapper, VersionInfo> implements IVersionInfoService {

    private final VersionInfoMapper versionInfoMapper;

    @Override
    public Map<String, Object> getConfig() {
        Map<String, Object> result = new HashMap<>(2);
        Map<String, Object> system = new HashMap<>(3);
        system.put("logo", "https://meedu-cos.meedu.xyz/images/FgY3kh3IdDhrJfb5NGueOVgol2WWeKRtwFP4lYUz.png");
        system.put("version", "v1.0.0");
        Map<String, String> url = new HashMap<>(3);
        url.put("api", "https://demo-api.meedu.xyz");
        url.put("h5", "https://h5.meedu.xyz");
        url.put("pc", "https://demo.meedu.xyz");
        system.put("url", url);
        Map<String, String> video = new HashMap<>(1);
        video.put("default_service", "tencent");
        result.put("video", video);
        result.put("system", system);
        return result;
    }

    @Override
    public VersionInfoVO getInfo() {
        VersionInfo versionInfo = versionInfoMapper.selectOne(new LambdaQueryWrapper<VersionInfo>()
                .eq(VersionInfo::getIsDelete, 0)
                .select(VersionInfo::getSpringbootVersion, VersionInfo::getGeekeduVersion, VersionInfo::getJdkVersion));
        return VersionInfoVO.of(versionInfo.getSpringbootVersion(), versionInfo.getGeekeduVersion(), versionInfo.getJdkVersion());
    }
}
