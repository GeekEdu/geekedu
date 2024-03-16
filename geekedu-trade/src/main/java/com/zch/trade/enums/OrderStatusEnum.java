package com.zch.trade.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @author Poison02
 * @date 2024/3/9
 */
@Getter
public enum OrderStatusEnum {

    ORDERED_NO_PAY(1, "已下单未支付"),
    ORDERED_AND_CANCELLED(2, "已下单已取消"),
    ORDERED_AND_PAID(3, "已下单已支付"),
    ORDERED_AND_REFUNDED(4, "已下单已退款"),
    ;

    @EnumValue
    private Integer code;

    private String value;

    OrderStatusEnum() {
    }

    OrderStatusEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }
}
