package com.zch.common.mvc.result;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Poison02
 * @date 2024/1/10
 */
@AllArgsConstructor
@NoArgsConstructor
public enum ResponseCode implements IResponseCode, Serializable {

    SUCCESS(0, "操作成功！"),
    SYSTEM_ERROR(400, "系统异常！")
    ;

    private Integer code;

    private String Message;

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return Message;
    }

    @Override
    public String toString() {
        return "ResponseCode{" +
                "code='" + code + '\'' +
                ", msg='" + Message + '\'' +
                '}';
    }

    public static ResponseCode getValue(Integer code){
        for (ResponseCode value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return SYSTEM_ERROR;
    }

}
