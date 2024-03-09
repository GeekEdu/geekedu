package com.zch.trade.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @author Poison02
 * @date 2024/3/9
 */
@Getter
public enum PayTypeEnum {

    ALIPAY(1, "支付宝"),
    WX_PAY(2, "微信"),
    ACCOUNT(3, "余额"),
    OTHER(4, "手动打款"),
    ;

    @EnumValue
    private Integer code;

    private String value;

    PayTypeEnum() {
    }

    PayTypeEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }
}
