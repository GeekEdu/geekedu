package com.zch.api.vo.trade.order;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Poison02
 * @date 2024/3/15
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OrderFullVO extends BaseVO {

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 最终支付金额
     */
    private BigDecimal amount;

    /**
     * 订单号
     */
    private String orderNumber;

    /**
     * 支付类型
     */
    private String payment;

    /**
     * 支付类型文本
     */
    private String payTypeText;

    /**
     * 订单状态文本
     */
    private String orderStatusText;

    /**
     * 是否退款
     */
    private Boolean isRefund;

    private LocalDateTime createdTime;

    /**
     * 商品信息
     */
    private GoodsVO goods;

}
