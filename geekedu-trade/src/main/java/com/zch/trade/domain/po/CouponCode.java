package com.zch.trade.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Poison02
 * @date 2024/4/17
 */
@Data
@TableName("coupon_code")
public class CouponCode {

    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;

    private String code;

    private Long couponId;

    private LocalDateTime createdTime;

    private LocalDateTime expiredTime;

    @TableLogic
    private Boolean isDelete;

}
