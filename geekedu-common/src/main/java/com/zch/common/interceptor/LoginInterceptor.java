package com.zch.common.interceptor;

import cn.dev33.satoken.stp.StpUtil;
import com.zch.common.context.UserContext;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Poison02
 * @date 2024/1/20
 */
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Long loginId = (Long) StpUtil.getLoginIdByToken(request.getHeader("Authorization"));
        // 存入 ThreadLocal
        UserContext.set("loginId", loginId);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        UserContext.remove();
    }

}
