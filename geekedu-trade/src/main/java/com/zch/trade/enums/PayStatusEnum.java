package com.zch.trade.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @author Poison02
 * @date 2024/3/16
 */
@Getter
public enum PayStatusEnum {

    HAVE_PAID(1, "已支付"),
    NON_PAYMENT(2, "未支付"),
    DURING_PAYMENT(3, "支付中"),
    CANCELLED(4, "已取消"),
    BEING_REFUNDED(5, "退款中"),
    REFUNDED(6, "已退款"),
    ;

    @EnumValue
    private Integer code;

    private String value;

    PayStatusEnum() {
    }

    PayStatusEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

}
