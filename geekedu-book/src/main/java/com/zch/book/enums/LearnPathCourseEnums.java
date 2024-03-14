package com.zch.book.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @author Poison02
 * @date 2024/3/13
 */
@Getter
public enum LearnPathCourseEnums {

    REPLAY_COURSE(1, "录播课"),
    LIVE_COURSE(2, "直播课"),
    IMAGE_TEXT(3, "图文"),
    E_BOOK(4, "电子书"),
    ;

    @EnumValue
    Integer code;

    String value;

    LearnPathCourseEnums() {
    }

    LearnPathCourseEnums(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public static LearnPathCourseEnums getByCode(Integer code) {
        for (LearnPathCourseEnums e : values()) {
            if (e.code.equals(code)) {
                return e;
            }
        }
        return null; // or throw an exception if 'code' is not found
    }

}
