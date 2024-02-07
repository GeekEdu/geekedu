package com.zch.oss.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum FilePathEnum {
    ALL(0, "/all/"),
    PPT(1, "/ppt/"),
    COURSE_COVER(2, "/course_cover/"),
    COURSE_DESC(3, "/course_desc"),
    ARTICLE(4, "/article/")
    ;
    @EnumValue
    final int value;
    final String path;

    FilePathEnum(int value, String path) {
        this.value = value;
        this.path = path;
    }

    public static String returnPath(int value) {
        for (FilePathEnum platform : FilePathEnum.values()) {
            if (platform.getValue() == value) {
                return platform.getPath();
            }
        }
        return null;
    }
}
