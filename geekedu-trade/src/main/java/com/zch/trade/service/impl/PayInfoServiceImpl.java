package com.zch.trade.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zch.api.dto.trade.pay.PayInfoForm;
import com.zch.api.feignClient.user.UserFeignClient;
import com.zch.api.vo.trade.pay.PayInfoVO;
import com.zch.common.core.utils.BeanUtils;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.common.satoken.context.UserContext;
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
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
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

    private final RocketMQTemplate rocketMQTemplate;

    private final UserFeignClient userFeignClient;

    @Override
    public String handleAliPay(String orderId, String scene, String payment, String redirect) {
        if (StringUtils.isNotBlank(orderId) && StringUtils.isNotBlank(scene) && StringUtils.isNotBlank(payment)
                && StringUtils.isNotBlank(redirect)) {
            AliSandboxPay alipay = new AliSandboxPay();
            // 查询订单相关信息
            Order order = orderService.getOne(new LambdaQueryWrapper<Order>()
                    .eq(Order::getOrderNumber, orderId));
            if (ObjectUtils.isNotNull(order)) {
                alipay.setTotalAmount(order.getAmount().doubleValue());
                alipay.setOutTraceNo(orderId);
                alipay.setSubject("GeekEdu-" + order.getGoodsType().getValue());
                alipay.setReturnUrl(redirect);
                // pc网站支付，则直接渲染的是form
                return payAdapter.websitePayment(alipay);
            }
        }
        return "";
    }

    @Override
    public String generateQrCode(String orderId, String redirect) {
        if (StringUtils.isNotBlank(orderId)) {
            AliSandboxPay alipay = new AliSandboxPay();
            // 查询订单相关信息
            Order order = orderService.getOne(new LambdaQueryWrapper<Order>()
                    .eq(Order::getOrderNumber, orderId));
            if (ObjectUtils.isNotNull(order)) {
                alipay.setOutTraceNo(orderId);
                alipay.setTotalAmount(order.getAmount().doubleValue());
                alipay.setSubject("GeekEdu-" + order.getGoodsType().getValue());
                alipay.setReturnUrl(redirect);
                return payAdapter.qrCodePayment(alipay);
            }
        }
        return "";
    }

    @Override
    public void handlePayNotify(HttpServletRequest request) {
        // 调用支付宝支付验签
        AliReturnPay notify = payAdapter.verificationAndNotify(request);
        if (ObjectUtils.isNotNull(notify)) {
            // 支付成功
            PayInfoForm form = new PayInfoForm();
            form.setPayAmount(StringUtils.isBlank(notify.getTotal_amount()) ? BigDecimal.ZERO : new BigDecimal(notify.getTotal_amount()));
            form.setOrderId(notify.getOut_trade_no());
            form.setIsPaid(false);
            // 更新支付信息
            updatePayInfo(form);
            // 同时更新订单信息
            sendUpdateOrderMsg(notify.getOut_trade_no());
        }
    }

    @Override
    public Boolean queryPayStatus(String orderId) {
        // 查找对应的支付信息
        PayInfo one = getOne(new LambdaQueryWrapper<PayInfo>()
                .eq(PayInfo::getOrderId, orderId));
        if (ObjectUtils.isNull(one)) {
            return false;
        }
        // 查询支付宝交易状态
        AliReturnPay aliPay = payAdapter.queryPayStatus(one.getOrderId());
        if (ObjectUtils.isNotNull(aliPay) && StringUtils.equals(aliPay.getTrade_status(), "TRADE_SUCCESS")) {
            // 支付成功
            PayInfoForm form = new PayInfoForm();
            form.setIsPaid(false);
            form.setPayAmount(new BigDecimal(aliPay.getTotal_amount()));
            form.setOrderId(aliPay.getOut_trade_no());
            // 更新支付信息
            updatePayInfo(form);
            // 同时更新订单信息
            sendUpdateOrderMsg(aliPay.getOut_trade_no());
            return true;
        }
        return false;
    }

    @Override
    public void createPayInfo(PayInfoForm form) {
        Long userId = Long.valueOf((String) StpUtil.getLoginId());
        // Long userId = 1745747394693820416L;
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
            if (ObjectUtils.isNotNull(payInfo) && ! payInfo.getIsPaid()) {
                payInfo.setIsPaid(true);
                payInfo.setPayTime(LocalDateTime.now());
                updateById(payInfo);
                // 更新用户积分
                Long point = payInfo.getPayAmount().divide(BigDecimal.valueOf(2), RoundingMode.HALF_UP)
                        .setScale(0, RoundingMode.HALF_UP).longValue();
                userFeignClient.updateUserPoint(payInfo.getUserId(), point);
            }
        }
    }

    @Override
    public PayInfoVO queryPayInfo(String orderId) {
        PayInfo one = getOne(new LambdaQueryWrapper<PayInfo>()
                .eq(PayInfo::getOrderId, orderId));
        if (ObjectUtils.isNull(one)) {
            return null;
        }
        PayInfoVO vo = new PayInfoVO();
        BeanUtils.copyProperties(one, vo);
        return vo;
    }

    /**
     * 发送更新订单消息
     * @param orderNumber
     */
    private void sendUpdateOrderMsg(String orderNumber) {
        Message<String> msg = MessageBuilder.withPayload(orderNumber).build();
        rocketMQTemplate.asyncSend("orderInfo-update-topic", msg, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.debug("更新订单信息-消息发送成功");
            }

            @Override
            public void onException(Throwable throwable) {
                log.debug("更新订单信息-消息发送成功");
            }
        });
    }

}
