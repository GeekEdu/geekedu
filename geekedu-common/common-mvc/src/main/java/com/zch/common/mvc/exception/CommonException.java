package com.zch.common.mvc.exception;

import com.zch.common.mvc.result.IResponseCode;
import lombok.Getter;

/**
 * @author Poison02
 * @date 2023/12/31
 */
@Getter
public class CommonException extends RuntimeException{
    public IResponseCode resultCode;

    public CommonException(IResponseCode errorCode) {
        super(errorCode.getMessage());
        this.resultCode = errorCode;
    }

    public CommonException(String message){
        super(message);
    }

    public CommonException(String message, Throwable cause){
        super(message, cause);
    }

    public CommonException(Throwable cause){
        super(cause);
    }
}
