package com.zch.course.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @author Poison02
 * @date 2024/3/12
 */
@Getter
public enum LiveVideoStatusEnum {

    NOT_STARTED(1, "未开始"),

    ALREADY_ENDED(2, "已结束"),
    ;

    @EnumValue
    Integer code;

    String value;

    LiveVideoStatusEnum() {
    }

    LiveVideoStatusEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }
}
