package com.zch.common.domain.result;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Poison02
 * @date 2023/12/28
 */
@Data
public class Response<T> implements Serializable {

    private Integer status;

    private T data;

    private String message;

    private long timestamp;

    public static <T> Response<T> success() {
        return success(null);
    }

    public static <T> Response<T> success(T data) {
        Response<T> Response = new Response<>();
        Response.setStatus(0);
        Response.setMessage(ResponseCode.SUCCESS.getMsg());
        Response.setData(data);
        Response.setTimestamp(new Date().getTime());
        return Response;
    }

    public static <T> Response<T> failed() {
        return Response(ResponseCode.SYSTEM_ERROR.getCode(), ResponseCode.SYSTEM_ERROR.getMsg(), null);
    }

    public static <T> Response<T> failed(String msg) {
        return Response(ResponseCode.SYSTEM_ERROR.getCode(), msg, null);
    }

    public static <T> Response<T> judge(boolean status) {
        if (status) {
            return success();
        } else {
            return failed();
        }
    }

    public static <T> Response<T> failed(IResponseCode ResponseCode) {
        return Response(ResponseCode.getCode(), ResponseCode.getMsg(), null);
    }

    public static <T> Response<T> failed(IResponseCode ResponseCode, String msg) {
        return Response(ResponseCode.getCode(), msg, null);
    }

    private static <T> Response<T> Response(IResponseCode ResponseCode, T data) {
        return Response(ResponseCode.getCode(), ResponseCode.getMsg(), data);
    }

    private static <T> Response<T> Response(Integer code, String msg, T data) {
        Response<T> Response = new Response<>();
        Response.setStatus(code);
        Response.setData(data);
        Response.setMessage(msg);
        Response.setTimestamp(new Date().getTime());
        return Response;
    }

    public static boolean isSuccess(Response<?> Response) {
        return Response != null && ResponseCode.SUCCESS.getCode().equals(Response.getStatus());
    }

}
