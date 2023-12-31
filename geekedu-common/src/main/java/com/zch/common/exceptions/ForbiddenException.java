package com.zch.common.exceptions;

import lombok.Getter;

/**
 * @author Poison02
 * @date 2024/1/6
 */
@Getter
public class ForbiddenException extends CommonException{
    public ForbiddenException(String message) {
        super(403, message);
    }

    public ForbiddenException(int code, String message) {
        super(code, message);
    }

    public ForbiddenException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
