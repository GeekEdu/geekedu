package com.zch.trade.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @author Poison02
 * @date 2024/3/12
 */
@Getter
public enum GoodsTypeEnums {

    VOD(1, "VOD"),
    LIVE(2, "LIVE"),
    BOOK(3, "BOOK"),
    VIP(4, "VIP"),
    NOT_V(5, "NOT_V"),
    ;

    @EnumValue
    Integer code;

    String value;

    GoodsTypeEnums() {
    }

    GoodsTypeEnums(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public static String getValueByCode(Integer code) {
        for (GoodsTypeEnums enums : GoodsTypeEnums.values()) {
            if (enums.code.equals(code)) {
                return enums.value;
            }
        }
        return null; // 或者你可以抛出异常或者返回一个默认值
    }
}
