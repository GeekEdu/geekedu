package com.zch.trade.adapter.aliSandbox;

import cn.hutool.json.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.common.pay.config.AliSandboxConfig;
import com.zch.trade.adapter.PayAdapter;
import com.zch.trade.domain.po.AliReturnPay;
import com.zch.trade.domain.po.AliSandboxPay;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Poison02
 * @date 2024/3/7
 */
@Component
public class AliSandboxPayAdapter implements PayAdapter {

    // 支付宝网关地址
    private static final String GATEWAY_URL = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";
    private static final String FORMAT = "JSON";
    private static final String CHARSET = "UTF-8";
    //签名方式
    private static final String SIGN_TYPE = "RSA2";

    @Resource
    private AliSandboxConfig aliPayConfig;

    @Override
    public String websitePayment(AliSandboxPay alipay) {
        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, aliPayConfig.getAppId(),
                aliPayConfig.getAppPrivateKey(), FORMAT, CHARSET, aliPayConfig.getAlipayPublicKey(), SIGN_TYPE);
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl(aliPayConfig.getNotifyUrl());
        request.setReturnUrl(alipay.getReturnUrl());
        // 示例
//        alipay.setTotalAmount(0.01);
//        alipay.setSubject("测试支付宝支付");
//        alipay.setTraceNo("654321999");
        request.setBizContent("{\"out_trade_no\":\"" + alipay.getTraceNo() + "\","
                + "\"total_amount\":\"" + alipay.getTotalAmount() + "\","
                + "\"subject\":\"" + alipay.getSubject() + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        String form = "";
        try {
            // 调用SDK生成表单
            form = alipayClient.pageExecute(request).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return form;
    }

    @Override
    public String qrCodePayment(AliSandboxPay alipay) {
        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, aliPayConfig.getAppId(),
                aliPayConfig.getAppPrivateKey(), FORMAT, CHARSET, aliPayConfig.getAlipayPublicKey(), SIGN_TYPE);
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        request.setNotifyUrl(aliPayConfig.getNotifyUrl());
        request.setReturnUrl(alipay.getReturnUrl());
        request.setBizContent("{\"out_trade_no\":\"" + alipay.getTraceNo() + "\","
                + "\"total_amount\":\"" + alipay.getTotalAmount() + "\","
                + "\"subject\":\"" + alipay.getSubject() + "\"}");
        String qr = "";
        try {
            AlipayTradePrecreateResponse response = alipayClient.execute (request);
            // 返回支付宝支付网址，用于生成二维码
            qr = response.getQrCode();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return qr;
    }

    @Override
    public AliReturnPay verificationAndNotify(HttpServletRequest request) {
        if (request.getParameter("trade_status").equals("TRADE_SUCCESS")) {
            AliReturnPay returnPay = new AliReturnPay();
            Map<String, String> params = new HashMap<>();
            Map<String, String[]> requestParams = request.getParameterMap();
            for (String name : requestParams.keySet()) {
                params.put(name, request.getParameter(name));
            }
            // 支付宝验签
            String sign = params.get("sign");
            String content = AlipaySignature.getSignCheckContentV1(params);
            boolean checkSignature = false;
            try {
                checkSignature = AlipaySignature.rsa256CheckContent(content, sign, aliPayConfig.getAlipayPublicKey(), "UTF-8"); // 验证签名
            } catch (AlipayApiException e) {
                e.printStackTrace();
            }
            if (checkSignature) {
                // 验签通过
                returnPay.setOut_trade_no(params.get("out_trade_no"));
                // returnPay.setSign(sign);
                returnPay.setTrade_status(params.get("trade_status"));
                returnPay.setTrade_no(params.get("trade_no"));
                returnPay.setTotal_amount(params.get("total_amount"));
                return returnPay;
//                System.out.println("交易名称: " + params.get("subject"));
//                System.out.println("交易状态: " + params.get("trade_status"));
//                System.out.println("支付宝交易凭证号: " + params.get("trade_no"));
//                System.out.println("商户订单号: " + params.get("out_trade_no"));
//                System.out.println("交易金额: " + params.get("total_amount"));
//                System.out.println("买家在支付宝唯一id: " + params.get("buyer_id"));
//                System.out.println("买家付款时间: " + params.get("gmt_payment"));
//                System.out.println("买家付款金额: " + params.get("buyer_pay_amount"));
            }
        }
        return null;
    }

    @Override
    public Boolean refund(AliSandboxPay alipay) {
        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL,
                aliPayConfig.getAppId(), aliPayConfig.getAppPrivateKey(), FORMAT, CHARSET,
                aliPayConfig.getAlipayPublicKey(), SIGN_TYPE);
        // 创建 Request，设置参数
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.set("trade_no", alipay.getAlipayTraceNo());  // 支付宝回调的订单流水号
        bizContent.set("refund_amount", alipay.getTotalAmount());  // 订单的总金额
        bizContent.set("out_request_no", alipay.getTraceNo());   //  我的订单编号

        // 执行退款
        boolean isRefund = false;
        try {
            AlipayTradeRefundResponse response = alipayClient.execute(request);
            if (ObjectUtils.isNotNull(response) && ObjectUtils.isNotNull(response.isSuccess())) {
                isRefund = response.isSuccess();
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return isRefund;
    }
}
