package com.zch.user.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @author Poison02
 * @date 2024/3/9
 */
@Getter
public enum ThumbEnums {

    IMAGE_TEXT(1, "图文点赞"),
    QA_COMMENT(2, "问答评论"),
    ;

    @EnumValue
    private Integer code;

    private String value;

    ThumbEnums() {
    }

    ThumbEnums(Integer code, String value) {
        this.code = code;
        this.value = value;
    }
}
