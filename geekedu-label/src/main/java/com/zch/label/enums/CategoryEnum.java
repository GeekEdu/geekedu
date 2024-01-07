package com.zch.label.enums;

import lombok.Getter;

/**
 * @author Poison02
 * @date 2024/1/7
 */
@Getter
public enum CategoryEnum {

    COURSE(1, "课程分类"),
    TUTORIAL(2, "教程分类"),
    ASK(3, "问答分类")
    ;

    int value;
    String desc;

    CategoryEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String returnDesc(int code) {
        for (CategoryEnum enums:
        CategoryEnum.values()){
            if (enums.getValue() == code) {
                return enums.getDesc();
            }
        }
        return null;
    }
}
