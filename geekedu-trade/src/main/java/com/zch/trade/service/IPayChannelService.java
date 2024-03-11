package com.zch.trade.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.vo.trade.PayChannelVO;
import com.zch.trade.domain.po.PayChannel;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/3/11
 */
public interface IPayChannelService extends IService<PayChannel> {

    /**
     * 查询所有支付渠道
     * @return
     */
    List<PayChannelVO> getPayChannelList();

}
