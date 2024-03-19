package com.zch.api.vo.trade.coupon;

import com.zch.common.mvc.entity.BaseVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Poison02
 * @date 2024/3/19
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CouponVO extends BaseVO {

    /**
     * 优惠券id
     */
    private Long couponId;

    /**
     * 优惠券金额
     */
    private BigDecimal couponPrice;

    /**
     * 优惠券描述
     */
    private String couponCode;

    /**
     * 优惠券类型
     */
    private String couponDesc;

    /**
     * 优惠券数量
     */
    private Integer couponTotal;

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
    private LocalDateTime expireTime;

}
