package com.zch.ask.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @author Poison02
 * @date 2024/2/20
 */
@Getter
public enum CommentsEnum {

    REPLAY_COURSE(1, "录播课"),
    REPLAY_COURSE_HOUR(2, "录播课课时"),
    LIVE_COURSE(3, "直播课"),
    IMAGE_TEXT(4, "图文"),
    E_BOOK(5, "电子书"),
    E_BOOK_ARTICLE(6, "电子书文章"),
    ASK_QUESTION(7, "问答"),
    ;

    @EnumValue
    private final int value;
    private final String desc;

    CommentsEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

}
