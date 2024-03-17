package com.zch.trade.adapter;

import com.zch.trade.domain.po.AliReturnPay;
import com.zch.trade.domain.po.AliSandboxPay;

import javax.servlet.http.HttpServletRequest;

/**
 * 支付适配器
 * @author Poison02
 * @date 2024/3/7
 */
public interface PayAdapter {

    /**
     * 电脑网站支付
     * @return
     */
    String websitePayment(AliSandboxPay alipay);

    /**
     * 二维码支付
     * @return 返回的直接就是支付地址的二维码url 前端可以直接使用antd的QrCode组件展示
     */
    String qrCodePayment(AliSandboxPay alipay);

    /**
     * 验签和回调
     * @param request
     * @return
     */
    AliReturnPay verificationAndNotify(HttpServletRequest request);

    /**
     * 退款
     * @param alipay
     * @return
     */
    Boolean refund(AliSandboxPay alipay);

}
