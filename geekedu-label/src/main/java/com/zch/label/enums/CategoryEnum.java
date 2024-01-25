package com.zch.label.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @author Poison02
 * @date 2024/1/7
 */
@Getter
public enum CategoryEnum {

    REPLAY_COURSE(1, "录播课"),
    LIVE_COURSE(2, "直播课"),
    IMAGE_TEXT(3, "图文"),
    E_BOOK(3, "电子书"),
    ASK(3, "学习路线")
    ;

    @EnumValue
    private final int value;
    private final String desc;

    CategoryEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
