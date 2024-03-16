package com.zch.user.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @author Poison02
 * @date 2024/3/9
 */
@Getter
public enum CollectionEnums {

    E_BOOK(1, "电子书点赞"),
    IMAGE_TEXT(2, "图文点赞")
    ;

    @EnumValue
    private Integer code;

    private String value;

    CollectionEnums() {
    }

    CollectionEnums(Integer code, String value) {
        this.code = code;
        this.value = value;
    }
}
