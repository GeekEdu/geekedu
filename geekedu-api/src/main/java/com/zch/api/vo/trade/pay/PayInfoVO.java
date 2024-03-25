package com.zch.api.vo.trade.pay;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Poison02
 * @date 2024/3/25
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PayInfoVO extends BaseVO {

    private Integer id;

    /**
     * 支付名称 对应渠道名
     */
    private String payName;

    /**
     * 支付金额
     */
    private BigDecimal payAmount;

    /**
     * 支付订单号
     */
    private String orderId;

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

}
