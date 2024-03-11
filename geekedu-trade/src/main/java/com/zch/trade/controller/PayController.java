package com.zch.trade.controller;

import com.zch.api.vo.trade.PayChannelVO;
import com.zch.common.mvc.result.Response;
import com.zch.trade.service.IPayChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Poison02
 * @date 2024/3/11
 */
@RestController
@RequestMapping("/api/pay")
@RequiredArgsConstructor
public class PayController {

    private final IPayChannelService payChannelService;

    /**
     * 返回所有支付渠道
     * @return
     */
    @GetMapping("/channels")
    public Response<List<PayChannelVO>> getPayChannels(@RequestParam("scene") String scene) {
        System.out.println("scene: " + scene);
        return Response.success(payChannelService.getPayChannelList());
    }

}
