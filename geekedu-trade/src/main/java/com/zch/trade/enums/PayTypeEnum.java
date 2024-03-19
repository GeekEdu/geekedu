package com.zch.trade.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @author Poison02
 * @date 2024/3/9
 */
@Getter
public enum PayTypeEnum {

    ALIPAY(1, "ALIPAY"),
    WX_PAY(2, "WX_PAY"),
    ACCOUNT(3, "余额"),
    OTHER(4, "OTHER"),
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
