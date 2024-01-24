package com.zch.common.mvc.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import com.zch.common.mvc.interceptor.LoginInterceptor;
import com.zch.common.mvc.interceptor.RefreshTokenInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Poison02
 * @date 2024/1/21
 */
//@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册SaToken注解拦截器
        registry.addInterceptor(new SaInterceptor()).addPathPatterns("/**");
        // 自定义拦截器
        registry.addInterceptor(new LoginInterceptor())
                .excludePathPatterns("/api/captcha/image", "/api/login")
                .order(1);
        // 刷新token拦截器
        registry.addInterceptor(new RefreshTokenInterceptor())
                .order(0);
    }
}
