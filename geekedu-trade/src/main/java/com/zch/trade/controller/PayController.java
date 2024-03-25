package com.zch.trade.controller;

import com.zch.api.dto.trade.pay.PayInfoForm;
import com.zch.api.vo.trade.PayChannelVO;
import com.zch.api.vo.trade.pay.PayInfoVO;
import com.zch.common.mvc.result.Response;
import com.zch.trade.domain.po.PayInfo;
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
     *
     * @return
     */
    @GetMapping("/channels")
    public Response<List<PayChannelVO>> getPayChannels(@RequestParam("scene") String scene) {
        System.out.println("scene: " + scene);
        return Response.success(payChannelService.getPayChannelList());
    }

    /**
     * 支付宝支付
     * @param orderId
     * @param scene
     * @param payment
     * @param redirect
     * @param response
     */
    @GetMapping("/aliPay")
    public void pay(@RequestParam("orderId") String orderId,
                    @RequestParam("scene") String scene,
                    @RequestParam("payment") String payment,
                    @RequestParam("redirect") String redirect,
                    HttpServletResponse response) {
        try {
            response.setContentType("text/html;charset=" + "UTF-8");
            // 直接将完整的表单html输出到页面
            response.getWriter().write(payInfoService.handleAliPay(orderId, scene, payment, redirect));
            response.getWriter().flush();
            response.getWriter().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成支付二维码
     * @param orderId
     * @return
     */
    @GetMapping("/qrCode")
    public Response<String> generateQrCode(@RequestParam("orderId") String orderId, @RequestParam("redirect") String redirect) {
        return Response.success(payInfoService.generateQrCode(orderId, redirect));
    }

    @PostMapping("/notify")
    public void aliPayNotify(HttpServletRequest request) {
        payInfoService.handlePayNotify(request);
    }

    /**
     * 查询支付状态
     * @param orderId
     * @return
     */
    @GetMapping("/queryPayStatus")
    public Response<Boolean> queryPayStatus(@RequestParam("orderId") String orderId) {
        return Response.success(payInfoService.queryPayStatus(orderId));
    }

    /**
     * 写入支付信息
     * @param form
     */
    @PostMapping("/create/payInfo")
    public void createPayInfo(@RequestBody PayInfoForm form) {
        payInfoService.createPayInfo(form);
    }

    /**
     * 查询支付信息
     * @param orderId
     * @return
     */
    @GetMapping("/info")
    public Response<PayInfoVO> queryPayInfo(@RequestParam("orderId") String orderId) {
        return Response.success(payInfoService.queryPayInfo(orderId));
    }

}
