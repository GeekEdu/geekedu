package com.zch.trade.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.vo.trade.PayChannelVO;
import com.zch.common.core.utils.BeanUtils;
import com.zch.common.core.utils.CollUtils;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.trade.domain.po.PayChannel;
import com.zch.trade.mapper.PayChannelMapper;
import com.zch.trade.service.IPayChannelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Poison02
 * @date 2024/3/11
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class PayChannelServiceImpl extends ServiceImpl<PayChannelMapper, PayChannel> implements IPayChannelService {

    private final PayChannelMapper payChannelMapper;

    @Override
    public List<PayChannelVO> getPayChannelList() {
        List<PayChannel> payChannels = payChannelMapper.selectList(new LambdaQueryWrapper<PayChannel>());
        if (ObjectUtils.isNull(payChannels) || CollUtils.isEmpty(payChannels)) {
            return new ArrayList<>(0);
        }
        return payChannels.stream().map(item -> {
            PayChannelVO vo = new PayChannelVO();
            BeanUtils.copyProperties(item, vo);
            return vo;
        }).collect(Collectors.toList());
    }

}
