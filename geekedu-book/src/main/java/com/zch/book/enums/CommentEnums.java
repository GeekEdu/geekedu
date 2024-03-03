package com.zch.book.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @author Poison02
 * @date 2024/3/3
 */
@Getter
public enum CommentEnums {

    IMAGE_TEXT(1, "图文"),
    E_BOOK(2, "电子书"),
    E_BOOK_ARTICLE(3, "电子书文章")
    ;

    @EnumValue
    private final Integer code;

    private final String desc;

    CommentEnums(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getDescByCode(Integer code) {
        for (CommentEnums commentEnum : CommentEnums.values()) {
            if (commentEnum.getCode().equals(code)) {
                return commentEnum.getDesc();
            }
        }
        return null; // 如果找不到对应的code值，返回null或者抛出异常等处理方式
    }
}
