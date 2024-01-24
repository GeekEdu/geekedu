package com.zch.common.mvc.interceptor;

import com.zch.common.redis.utils.RedisUtils;
import com.zch.common.satoken.context.UserContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.zch.common.redis.constants.RedisConstants.*;

/**
 * 这个拦截器不管什么请求都会放行，唯一的作用就是刷新token
 * @author Poison02
 * @date 2024/1/23
 */
public class RefreshTokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取token
        String BearerToken = request.getHeader("Authorization");
        // 为空不用拦截
        if (StringUtils.isBlank(BearerToken)) {
            return true;
        }
        String token = BearerToken.substring(7);
        // 保存userId
        Long userId = Long.valueOf(RedisUtils.getCacheObject(LOGIN_USER_TOKEN + token));
        UserContext.set("userId", userId);
        // 刷新redis中的所有token和session
        RedisUtils.expire(LOGIN_USER_TOKEN + token, LOGIN_USER_TOKEN_TTL);
        RedisUtils.expire(AUTHORIZATION_LOGIN_TOKEN + token, LOGIN_USER_TOKEN_TTL);
        RedisUtils.expire(AUTHORIZATION_LOGIN_SESSION + userId, LOGIN_USER_TOKEN_TTL);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.remove();
    }
}
