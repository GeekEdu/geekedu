package com.zch.common.domain.result;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Poison02
 * @date 2023/12/28
 */
@Data
public class Response<T> implements Serializable {

    private String code;

    private T data;

    private String msg;

    public static <T> Response<T> success() {
        return success(null);
    }

    public static <T> Response<T> success(T data) {
        Response<T> Response = new Response<>();
        Response.setCode(ResponseCode.SUCCESS.getCode());
        Response.setMsg(ResponseCode.SUCCESS.getMsg());
        Response.setData(data);
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

    private static <T> Response<T> Response(String code, String msg, T data) {
        Response<T> Response = new Response<>();
        Response.setCode(code);
        Response.setData(data);
        Response.setMsg(msg);
        return Response;
    }

    public static boolean isSuccess(Response<?> Response) {
        return Response != null && ResponseCode.SUCCESS.getCode().equals(Response.getCode());
    }

}
