package com.zch.common.mvc.exception;

/**
 * @author Poison02
 * @date 2024/1/5
 */
public class DbException extends CommonException{
    public DbException(String message) {
        super(message);
    }

    public DbException(int code, String message) {
        super(message);
    }

    public DbException(int code, String message, Throwable cause) {
        super(message, cause);
    }
}
