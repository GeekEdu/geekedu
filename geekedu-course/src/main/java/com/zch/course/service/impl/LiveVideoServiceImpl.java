package com.zch.course.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.course.domain.po.LiveVideo;
import com.zch.course.mapper.LiveVideoMapper;
import com.zch.course.service.ILiveVideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Poison02
 * @date 2024/3/12
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class LiveVideoServiceImpl extends ServiceImpl<LiveVideoMapper, LiveVideo> implements ILiveVideoService {
}
