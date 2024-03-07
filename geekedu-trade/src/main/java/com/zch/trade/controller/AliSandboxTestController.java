package com.zch.trade.controller;

import cn.hutool.json.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.zch.common.pay.config.AliSandboxConfig;
import com.zch.trade.domain.po.AliSandboxPay;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Poison02
 * @date 2024/3/7
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/aliPay")
public class AliSandboxTestController {

    // 支付宝网关地址
    private static final String GATEWAY_URL = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";
    private static final String FORMAT = "JSON";
    private static final String CHARSET = "UTF-8";
    //签名方式
    private static final String SIGN_TYPE = "RSA2";

    @Resource
    private AliSandboxConfig aliPayConfig;

    @GetMapping("/test")
    public void pay(HttpServletResponse response) throws IOException {
        // 1. 创建 client
        AlipayClient client = new DefaultAlipayClient(GATEWAY_URL, aliPayConfig.getAppId(),
                aliPayConfig.getAppPrivateKey(), FORMAT, CHARSET, aliPayConfig.getAlipayPublicKey(), SIGN_TYPE);

        // 2. 创建 Request 并是个直Request参数
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        AliSandboxPay aliPay = new AliSandboxPay();
        aliPay.setTraceNo("32355342152");
        aliPay.setSubject("测试支付宝支付");
        aliPay.setTotalAmount(123.00);
        // 回调地址
        request.setNotifyUrl(aliPayConfig.getNotifyUrl());
        JSONObject bizContent = new JSONObject();
        // 自己生成的订单编号
        bizContent.set("out_trade_no", aliPay.getTraceNo());
        // 订单总金额
        bizContent.set("total_amount", aliPay.getTotalAmount());
        // 支付名称
        bizContent.set("subject", aliPay.getSubject());
        // 固定配置
        bizContent.set("product_code", "FAST_INSTANT_TRADE_PAY");
        request.setBizContent(bizContent.toString());

        // 执行请求 拿到相应的结果 返回给浏览器
        String form = "";
        try {
            // 调用sdk生成表单
            form = client.pageExecute(request).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        response.setContentType("text/html;charset=" + CHARSET);
        // 直接将完整表单html输出到页面
        response.getWriter().write(form);
        response.getWriter().flush();
        response.getWriter().close();
    }

    @PostMapping("/notify")
    public String payNotify(HttpServletRequest request) throws AlipayApiException {
        if (request.getParameter("trade_status").equals("TRADE_SUCCESS")) {
            System.out.println("=======支付宝回调==========");
            Map<String, String> params = new HashMap<>();
            Map<String, String[]> requestParams = request.getParameterMap();
            for (String name : requestParams.keySet()) {
                params.put(name, request.getParameter(name));
                System.out.println(name + "=" + request.getParameter(name));
            }
            String tradeNo = params.get("out_trade_no");
            String gmtPayment = params.get("gmt_payment");
            String alipayTradeNo = params.get("trade_no");

            String sign = params.get("sign");
            String content = AlipaySignature.getSignCheckContentV1(params);
            boolean checkSignature = AlipaySignature.rsa256CheckContent(content, sign, aliPayConfig.getAlipayPublicKey(), CHARSET);
            // 支付宝验签
            if (checkSignature) {
                // 验签通过
                System.out.println("交易名称: " + params.get("subject"));
                System.out.println("交易状态: " + params.get("trade_status"));
                System.out.println("支付宝交易凭证号: " + params.get("trade_no"));
                System.out.println("商户订单号: " + params.get("out_trade_no"));
                System.out.println("交易金额: " + params.get("total_amount"));
                System.out.println("买家在支付宝唯一id: " + params.get("buyer_id"));
                System.out.println("买家付款时间: " + params.get("gmt_payment"));
                System.out.println("买家付款金额: " + params.get("buyer_pay_amount"));

                // 更新订单未已支付
            }
        }
        return "success";
    }

    @GetMapping("/return")
    public String returnPay(@RequestBody AliSandboxPay aliPay) throws AlipayApiException {
        // 7天无理由退款
        // 1. 创建Client，通用SDK提供的Client，负责调用支付宝的API
        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL,
                aliPayConfig.getAppId(), aliPayConfig.getAppPrivateKey(), FORMAT, CHARSET,
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhS0WfyW3/4sSe9lgZXTKKjiQvz6l+oxxoqwKb/KRxv5QswIuvajk8JTgmjJisW6AA+oBNMBsbLaM5CAFRtvjIvVXvFH4sZG8QhO1eRzDXb4i+cw/ksSNwgdtiGnkFoRDflsixdzGDNj0wVVG9h7QiwJBSLunhqiQWE36c6Kni29UXEXchPKiJ6ayzlmryaRaJ7EoVgCPXAs63umi3X3GPEeyfzfLmrk6GEkgWL/XZUhrZaDFeaVVP1lfbyZpUjK7oDG+v81lWlwETh8seNshD4iQFEYt2jero4ja63z9/ZvL/aYxpfo26BVajulhx6oNE1Bm1IxRo1rkdQcZlI3wJwIDAQAB", SIGN_TYPE);
        // 2. 创建 Request，设置参数
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.set("trade_no", aliPay.getAlipayTraceNo());  // 支付宝回调的订单流水号
        bizContent.set("refund_amount", aliPay.getTotalAmount());  // 订单的总金额
        bizContent.set("out_request_no", aliPay.getTraceNo());   //  我的订单编号

        // 返回参数选项，按需传入
        //JSONArray queryOptions = new JSONArray();
        //queryOptions.add("refund_detail_item_list");
        //bizContent.put("query_options", queryOptions);

        request.setBizContent(bizContent.toString());

        // 3. 执行请求
        AlipayTradeRefundResponse response = alipayClient.execute(request);
        if (response.isSuccess()) {  // 退款成功，isSuccess 为true
            System.out.println("调用成功");

            // 4. 更新数据库状态

            return "成功";
        } else {   // 退款失败，isSuccess 为false
            System.out.println(response.getBody());
            return response.getBody();
        }

    }

}
