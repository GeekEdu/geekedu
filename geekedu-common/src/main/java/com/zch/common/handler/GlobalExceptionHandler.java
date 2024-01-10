package com.zch.common.handler;

import com.zch.common.domain.result.Response;
import com.zch.common.exceptions.CommonException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 * @author Poison02
 * @date 2024/1/9
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CommonException.class)
    public Response handleCommonException(CommonException e) {
        return Response.failed(e.getMessage());
    }

}
