package com.zch.common.domain.result;

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

    SUCCESS("00000", "成功！"),
    SYSTEM_ERROR("99999", "系统异常！")
    ;

    private String code;

    private String msg;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return "ResponseCode{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }

    public static ResponseCode getValue(String code){
        for (ResponseCode value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return SYSTEM_ERROR;
    }

}
