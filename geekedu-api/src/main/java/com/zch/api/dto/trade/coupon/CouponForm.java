package com.zch.api.dto.trade.coupon;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Poison02
 * @date 2024/3/19
 */
@Data
public class CouponForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 优惠券编码 TODO 待改善 由后端生成
     */
    private String couponCode;

    /**
     * 优惠券金额
     */
    private BigDecimal couponPrice;

    /**
     * 优惠券数量
     */
    private Integer couponTotal;

    /**
     * 限制使用次数
     */
    private Integer useLimit;

    /**
     * 优惠券描述
     */
    private String couponDesc;

    /**
     * 优惠券金额
     */
    private BigDecimal couponLimit;

    /**
     * 优惠券过期时间
     */
    private LocalDateTime expiredTime;

}
