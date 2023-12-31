package com.zch.common.domain;

import com.zch.common.constants.Constant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.slf4j.MDC;

import java.util.Date;

import static com.zch.common.constants.ErrorInfo.Code.FAILED;
import static com.zch.common.constants.ErrorInfo.Code.SUCCESS;
import static com.zch.common.constants.ErrorInfo.Msg.OK;

/**
 * @author Poison02
 * @date 2023/12/28
 */
@Data
@ApiModel(description = "通用响应结果")
public class Response<T> {

    @ApiModelProperty(value = "业务状态码，200-成功，其它-失败")
    private int code;
    @ApiModelProperty(value = "响应消息", example = "OK")
    private String msg;
    @ApiModelProperty(value = "响应数据")
    private T data;
    @ApiModelProperty(value = "当前时间戳", example = "1593852985")
    private Long timestamp;

    public static Response<Void> ok() {
        return new Response<Void>(SUCCESS, OK, null);
    }

    public static <T> Response<T> ok(T data) {
        return new Response<>(SUCCESS, OK, data);
    }

    public static <T> Response<T> error(String msg) {
        return new Response<>(FAILED, msg, null);
    }

    public static <T> Response<T> error(int code, String msg) {
        return new Response<>(code, msg, null);
    }

    public Response() {
    }

    public Response(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.timestamp = new Date().getTime();
    }

    public boolean success(){
        return code == SUCCESS;
    }

}
