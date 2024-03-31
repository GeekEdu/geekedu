package com.zch.api.vo.trade.coupon;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Poison02
 * @date 2024/3/31
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserCouponVO extends BaseVO {

    /**
     * 优惠券id
     */
    private Long couponId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 领取时间
     */
    private LocalDateTime receiveTime;

    /**
     * 是否使用
     */
    private Boolean isUsed;

    /**
     * 是否过期
     */
    private Boolean isExpired;

    /**
     * 优惠券使用时间
     */
    private LocalDateTime usedTime;

    /**
     * 优惠券金额
     */
    private BigDecimal couponPrice;

    /**
     * 优惠券使用码
     */
    private String couponCode;

    /**
     * 优惠券描述
     */
    private String couponDesc;

    /**
     * 优惠券使用限制
     */
    private BigDecimal CouponLimit;

    /**
     * 优惠券使用次数
     */
    private Integer useLimit;

    /**
     * 优惠券创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 优惠券过期时间
     */
    private LocalDateTime expiredTime;

}
