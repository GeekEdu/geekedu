package com.zch.trade.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @author Poison02
 * @date 2024/3/9
 */
@Getter
public enum ProductTypeEnum {

    REPLAY_COURSE(1, "录播课"),
    LIVE_COURSE(2, "直播课"),
    IMAGE_TEXT(3, "图文"),
    LEARN_PATH(4, "学习路径"),
    E_BOOK(5, "电子书"),
    ;

    @EnumValue
    private Integer code;

    private String value;

    ProductTypeEnum() {
    }

    ProductTypeEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }
}
