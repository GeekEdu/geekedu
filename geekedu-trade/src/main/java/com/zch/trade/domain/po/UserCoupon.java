package com.zch.trade.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Poison02
 * @date 2024/3/31
 */
@Data
@TableName("user_coupon")
public class UserCoupon {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 优惠券id
     */
    private Long couponId;

    /**
     * 领取时间
     */
    private LocalDateTime receiveTime;

    /**
     * 是否使用
     */
    private Boolean isUsed;

    /**
     * 使用时间
     */
    private LocalDateTime usedTime;

}
