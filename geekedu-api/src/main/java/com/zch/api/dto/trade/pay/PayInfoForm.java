package com.zch.api.dto.trade.pay;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Poison02
 * @date 2024/3/17
 */
@Data
public class PayInfoForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 支付名称
     */
    private String payName;

    /**
     * 支付渠道
     */
    private String payChannel;

    /**
     * 支付金额
     */
    private BigDecimal payAmount;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 支付时间
     */
    private LocalDateTime payTime;

    /**
     * 是否支付
     */
    private Boolean isPaid;

    /**
     * 支付订单号
     */
    private String orderId;

    /**
     * 交易订单号
     */
    private String tradeNo;

}
