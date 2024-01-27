package com.zch.ask.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.ask.domain.po.AskDetail;
import com.zch.ask.mapper.AskMapper;
import com.zch.ask.service.IAskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Poison02
 * @date 2024/1/27
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AskServiceImpl extends ServiceImpl<AskMapper, AskDetail> implements IAskService {
}
