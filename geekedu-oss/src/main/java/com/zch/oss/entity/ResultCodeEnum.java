package com.zch.oss.entity;

import lombok.Getter;

/**
 * @author Poison02
 * @date 2023/12/22
 */
@Getter
public enum ResultCodeEnum {

    SUCCESS(200, "成功"),

    FAIL(500, "失败");

    private int code;;

    private String msg;

    ResultCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static ResultCodeEnum getByCode(int code) {
        for (ResultCodeEnum resultCodeEnum : ResultCodeEnum.values()) {
            if (resultCodeEnum.getCode() == code) {
                return resultCodeEnum;
            }
        }
        return null;
    }
}
