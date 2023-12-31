package com.zch.common.exceptions;

import lombok.Getter;

/**
 * @author Poison02
 * @date 2023/12/31
 */
@Getter
public class BadRequestException extends CommonException{
    private final int status = 400;

    public BadRequestException(String message) {
        super(400, message);
    }

    public BadRequestException(int code, String message) {
        super(code, message);
    }

    public BadRequestException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
