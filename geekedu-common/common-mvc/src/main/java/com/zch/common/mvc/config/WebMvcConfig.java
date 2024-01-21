package com.zch.common.mvc.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import com.zch.common.mvc.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Poison02
 * @date 2024/1/21
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册SaToken注解拦截器
        registry.addInterceptor(new SaInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/user/api/login");
    }
}
