package com.zch.trade.controller;

import com.zch.api.vo.trade.PayChannelVO;
import com.zch.common.mvc.result.Response;
import com.zch.trade.service.IPayChannelService;
import com.zch.trade.service.IPayInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    private final IPayInfoService payInfoService;

    /**
     * 返回所有支付渠道
     * @return
     */
    @GetMapping("/channels")
    public Response<List<PayChannelVO>> getPayChannels(@RequestParam("scene") String scene) {
        System.out.println("scene: " + scene);
        return Response.success(payChannelService.getPayChannelList());
    }

    @GetMapping("/aliPay")
    public Response<String> pay(@RequestParam("orderId") String orderId,
                    @RequestParam("scene") String scene,
                    @RequestParam("payment") String payment,
                    @RequestParam("redirect") String redirect,
                    HttpServletResponse response) {
        if ("pc".equals(scene)) {
           try {
               response.setContentType("text/html;charset=" + "UTF-8");
               // 直接将完整的表单html输出到页面
               response.getWriter().write(payInfoService.handleAliPay(orderId, scene, payment, redirect));
               response.getWriter().flush();
               response.getWriter().close();
           } catch (Exception e) {
               e.printStackTrace();
           }
           return Response.success("PC PAY");
        } else if ("qrCode".equals(scene)){
            return Response.success(payInfoService.handleAliPay(orderId, scene, payment, redirect));
        }
        return Response.success("NOT FOUND");
    }

    @PostMapping("/notify")
    public void aliPayNotify(HttpServletRequest request) {
        payInfoService.handlePayNotify(request);
    }

}
