package com.zch.oss.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum Platform {
    TENCENT(1, "cos"),
    ALI(2, "oss"),
    QI_NIU(3, "qos"),
    ;
    @EnumValue
    private final int value;
    private final String desc;

    Platform(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
