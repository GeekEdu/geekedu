package com.zch.trade.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.dto.trade.pay.PayInfoForm;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.trade.adapter.PayAdapter;
import com.zch.trade.domain.po.AliReturnPay;
import com.zch.trade.domain.po.AliSandboxPay;
import com.zch.trade.domain.po.Order;
import com.zch.trade.domain.po.PayInfo;
import com.zch.trade.enums.PayTypeEnum;
import com.zch.trade.mapper.PayInfoMapper;
import com.zch.trade.service.IOrderService;
import com.zch.trade.service.IPayInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Poison02
 * @date 2024/3/9
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class PayInfoServiceImpl extends ServiceImpl<PayInfoMapper, PayInfo> implements IPayInfoService {

    private final PayAdapter payAdapter;

    private final IOrderService orderService;

    @Override
    public String handleAliPay(String orderId, String scene, String payment, String redirect) {
        if (StringUtils.isNotBlank(orderId) && StringUtils.isNotBlank(scene) && StringUtils.isNotBlank(payment)
                && StringUtils.isNotBlank(redirect)) {
            AliSandboxPay alipay = new AliSandboxPay();
            // 查询订单相关信息
            Order order = orderService.getById(orderId);
            if (ObjectUtils.isNotNull(order)) {
                alipay.setTotalAmount(order.getAmount().doubleValue());
                if (ObjectUtils.isNotNull(PayTypeEnum.valueOf(payment)) && payment.equals(PayTypeEnum.ALIPAY.getValue())) {
                    alipay.setTraceNo(order.getOrderId() + "");
                    alipay.setSubject("GeekEdu-" + order.getGoodsType().getValue());
                    alipay.setReturnUrl(redirect);
                    if ("pc".equals(scene)) {
                        // pc网站支付，则直接渲染的是form
                        return payAdapter.websitePayment(alipay);
                    } else if ("qrCode".equals(scene)) {
                        // 扫码支付，返回二维码链接
                        return payAdapter.qrCodePayment(alipay);
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void handlePayNotify(HttpServletRequest request) {
        // 调用支付宝支付验签
        AliReturnPay notify = payAdapter.verificationAndNotify(request);
        if (ObjectUtils.isNotNull(notify)) {
            // 支付成功
            PayInfoForm form = new PayInfoForm();
            form.setPayAmount(StringUtils.isBlank(notify.getTotal_amount()) ? BigDecimal.ZERO : new BigDecimal(notify.getTotal_amount()));
            form.setOrderId(Long.valueOf(notify.getTrade_no()));
            form.setIsPaid(false);
            // 更新订单信息
            updatePayInfo(form);
        }
    }

    @Override
    public void createPayInfo(PayInfoForm form) {
        // Long userId = UserContext.getLoginId();
        Long userId = 1745747394693820416L;
        // 查询支付信息
        if (ObjectUtils.isNotNull(form) && ObjectUtils.isNotNull(form.getOrderId())) {
            PayInfo one = getOne(new LambdaQueryWrapper<PayInfo>()
                    .eq(PayInfo::getOrderId, form.getOrderId()));
            if (ObjectUtils.isNull(one)) {
                PayInfo pay = new PayInfo();
                pay.setPayChannel(StringUtils.isBlank(form.getPayChannel())
                        ? PayTypeEnum.ALIPAY
                        : PayTypeEnum.valueOf(form.getPayChannel()));
                pay.setOrderId(form.getOrderId());
                pay.setPayName(form.getPayName());
                pay.setPayAmount(form.getPayAmount());
                pay.setUserId(userId);
                pay.setIsPaid(false);
                save(pay);
            }
        }
    }

    @Override
    public void updatePayInfo(PayInfoForm form) {
        if (ObjectUtils.isNotNull(form) && ObjectUtils.isNotNull(form.getOrderId())) {
            PayInfo payInfo = getOne(new LambdaQueryWrapper<PayInfo>()
                    .eq(PayInfo::getOrderId, form.getOrderId()));
            if (ObjectUtils.isNotNull(payInfo) && payInfo.getIsPaid()) {
                payInfo.setIsPaid(true);
                payInfo.setPayTime(LocalDateTime.now());
                updateById(payInfo);
            }
        }
    }

}
