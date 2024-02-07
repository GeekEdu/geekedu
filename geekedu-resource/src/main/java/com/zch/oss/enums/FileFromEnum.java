package com.zch.oss.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * 上传图片来源
 * @author Poison02
 * @date 2024/2/7
 */
@Getter
public enum FileFromEnum {

    ALL(0, "全部图片"),
    PPT(1, "PPT"),
    COURSE_COVER(2, "课程封面"),
    COURSE_DESC(3, "课程详情页"),
    ARTICLE(4, "文章配图")
    ;

    @EnumValue
    private final Integer code;

    private final String value;

    FileFromEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public static FileFromEnum fromCode(Integer code) {
        for (FileFromEnum e : FileFromEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null; // 如果找不到对应的枚举值，返回null或者抛出异常，具体视情况而定
    }

}
