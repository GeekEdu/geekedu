package com.zch.exam.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @author Poison02
 * @date 2024/2/25
 */
@Getter
public enum ExamTagsEnum {

    QUESTIONS(1, "题目"),
    PAPERS(2, "试卷");

    @EnumValue
    private final Integer code;

    private final String value;

    ExamTagsEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }
}
