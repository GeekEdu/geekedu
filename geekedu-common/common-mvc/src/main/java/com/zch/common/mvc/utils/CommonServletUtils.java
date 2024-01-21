package com.zch.common.mvc.utils;

import com.zch.common.mvc.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 通用 servlet 工具类 获取当前 request 和 response
 * @author Poison02
 * @date 2024/1/8
 */
@Slf4j
public class CommonServletUtils {

    /**
     * 获取当前请求的 request
     * @return
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes servletRequestAttributes;
         try {
             servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
         } catch (Exception e) {
             log.error(">>> 非Web上下文无法获取Request：", e);
             throw new CommonException("非Web上下文无法获取Request");
         }
         if (servletRequestAttributes == null) {
             throw new CommonException("非Web上下文无法获取Request");
         } else {
             return servletRequestAttributes.getRequest();
         }
    }

    /**
     * 获取当前请求的 response
     * @return
     */
    public static HttpServletResponse getResponse() {
        ServletRequestAttributes servletRequestAttributes;
        try {
            servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        } catch (Exception e) {
            log.error(">>> 非Web上下文无法获取Request：", e);
            throw new CommonException("非Web上下文无法获取Request");
        }
        if (servletRequestAttributes == null) {
            throw new CommonException("非Web上下文无法获取Request");
        } else {
            return servletRequestAttributes.getResponse();
        }
    }

}
