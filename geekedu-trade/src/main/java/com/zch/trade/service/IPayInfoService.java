package com.zch.trade.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zch.api.dto.trade.pay.PayInfoForm;
import com.zch.trade.domain.po.PayInfo;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Poison02
 * @date 2024/3/9
 */
public interface IPayInfoService extends IService<PayInfo> {

    /**
     * 处理支付宝支付
     * @param orderId
     * @param scene
     * @param payment
     * @param redirect
     * @return
     */
    String handleAliPay(String orderId, String scene, String payment, String redirect);

    /**
     * 生成二维码
     * @param orderId
     * @return
     */
    String generateQrCode(String orderId, String redirect);

    /**
     * 处理支付回调
     * @param request
     */
    void handlePayNotify(HttpServletRequest request);

    /**
     * 查询支付状态
     * @param orderId
     * @return
     */
    Boolean queryPayStatus(String orderId);

    /**
     * 创建订单信息
     * @param form
     */
    void createPayInfo(PayInfoForm form);

    /**
     * 更新订单信息
     * @param form
     */
    void updatePayInfo(PayInfoForm form);

}
