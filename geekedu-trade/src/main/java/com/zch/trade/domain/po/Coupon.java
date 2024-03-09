package com.zch.trade.domain.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zch.trade.enums.CouponTypeEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 优惠券表
 * @author Poison02
 * @date 2024/3/9
 */
@TableName("t_coupon")
@Data
public class Coupon {

    /**
     * 优惠券id
     */
    @TableId
    private Long couponId;

    /**
     * 抵扣金额
     */
    private BigDecimal couponPrice;

    /**
     * 优惠码
     */
    private String couponCode;

    /**
     * 数量
     */
    private Integer couponTotal;

    /**
     * 描述
     */
    private String couponDesc;

    /**
     * 使用的最低消费
     */
    private BigDecimal couponLimit;

    /**
     * 每个用户限制使用次数，默认 1
     */
    private Integer useLimit;

    /**
     * 优惠券类型 1-录播课，2-直播课，3-图文，4-学习路径
     */
    private CouponTypeEnum couponType;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 过期时间
     */
    private LocalDateTime expireTime;

    @TableLogic
    private Boolean isDelete;

}
