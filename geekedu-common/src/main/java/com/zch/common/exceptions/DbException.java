package com.zch.common.exceptions;

/**
 * @author Poison02
 * @date 2024/1/5
 */
public class DbException extends CommonException{
    public DbException(String message) {
        super(message);
    }

    public DbException(int code, String message) {
        super(code, message);
    }

    public DbException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
