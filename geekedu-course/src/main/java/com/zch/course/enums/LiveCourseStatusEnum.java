package com.zch.course.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @author Poison02
 * @date 2024/3/12
 */
@Getter
public enum LiveCourseStatusEnum {

    NOT_LISTED(1, "未开课"),
    ALREADY_LISTED(2, "已开课"),
    FINISHED(3, "已完结"),
    ;

    @EnumValue
    Integer code;

    String value;

    LiveCourseStatusEnum() {
    }

    LiveCourseStatusEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }
}
