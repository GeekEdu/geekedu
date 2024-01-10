package com.zch.common.handler;

import com.zch.common.domain.result.Response;
import com.zch.common.exceptions.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 * @author Poison02
 * @date 2024/1/9
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(CommonException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public <T> Response<T> handleCommonException(CommonException e) {
        log.error("Common Exception: ->>>: {}", e.getMessage());
        if (e.getResultCode() != null) {
            return Response.failed(e.getResultCode());
        }
        return Response.failed(e.getMessage());
    }

}
