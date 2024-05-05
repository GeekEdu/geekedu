package com.zch.common.mvc.config;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.same.SaSameUtil;
import cn.dev33.satoken.util.SaResult;
import com.zch.common.mvc.interceptor.LoginInterceptor;
import com.zch.common.mvc.interceptor.RefreshTokenInterceptor;
import org.springframework.context.annotation.Bean;
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
        // 自定义拦截器
        registry.addInterceptor(new LoginInterceptor())
                .excludePathPatterns("/api/captcha/image", "/api/login", "/api/v2/login/password", "/api/getUserById", "/api/thumb/count/{id}", "/api/collection/count",
                        "/api/v2/config", "/api/v2/navs", "/api/v2/sliders", "/api/v2/links", "/api/v2/announcement/{id}",
                        "/api/v2/announcement", "/api/v2/announcement/latest", "/api/v2/ask/config", "/api/v2/eBook/recommend", "/api/v2/imageText/recommend",
                        "/api/index/v2/block", "/api/index/mobile", "/api/member/vip/list", "/api/index/mobile", "/api/v2/category/list", "/api/v2/courses", "/api/v2/detail/{id}",
                        "/api/v2/{id}/comments", "/api/v2/learnedCourse", "/api/v2/{id}/learnedDetail", "/api/live/course/v2/list",
                        "/api/getCourseSimpleById/{id}", "/api/getCourseById/{id}", "/api/live/course/{id}/detail", "/api/v2/search",
                        "/api/live/course/v2/{id}/detail", "/api/live/course/v2/{id}/comments", "/api/live/course/getCourseSimpleById/{id}", "/api/eBook/v2/book/list", "/api/eBook/v2/book/{id}/detail",
                        "/api/eBook/v2/book/{id}/comments", "/api/eBook/v2/study/list", "/api/imageText/v2/list", "/api/imageText/v2/{id}/detail",
                        "/api/imageText/v2/{id}/comments", "/api/imageText/v2/study/list", "/api/path/v2/list", "/api/path/v2/{id}/detail",
                        "/api/question/v2/list", "/api/question/v2/category/list", "/api/question/v2/detail/{id}", "/api/comments/v2/list",
                        "/api/category/getCategoryById", "/api/category/getCategoryList", "/api/category/getCategorySimpleList", "/api/category/getCategoryPage")
                .order(1);
        // 刷新token拦截器
        registry.addInterceptor(new RefreshTokenInterceptor())
                .excludePathPatterns("/api/captcha/image", "/api/login", "/api/v2/login/password", "/api/getUserById", "/api/thumb/count/{id}", "/api/collection/count",
                        "/api/v2/config", "/api/v2/navs", "/api/v2/sliders", "/api/v2/links", "/api/v2/announcement/{id}",
                        "/api/v2/announcement", "/api/v2/announcement/latest", "/api/v2/ask/config", "/api/v2/eBook/recommend", "/api/v2/imageText/recommend",
                        "/api/index/v2/block", "/api/index/mobile", "/api/member/vip/list", "/api/index/mobile", "/api/v2/category/list", "/api/v2/courses", "/api/v2/detail/{id}",
                        "/api/v2/{id}/comments", "/api/v2/learnedCourse", "/api/v2/{id}/learnedDetail", "/api/live/course/v2/list",
                        "/api/getCourseSimpleById/{id}", "/api/getCourseById/{id}", "/api/live/course/{id}/detail", "/api/v2/search",
                        "/api/live/course/v2/{id}/detail", "/api/live/course/v2/{id}/comments", "/api/live/course/getCourseSimpleById/{id}", "/api/eBook/v2/book/list", "/api/eBook/v2/book/{id}/detail",
                        "/api/eBook/v2/book/{id}/comments", "/api/eBook/v2/study/list", "/api/imageText/v2/list", "/api/imageText/v2/{id}/detail",
                        "/api/imageText/v2/{id}/comments", "//api/imageText/v2/study/list", "/api/path/v2/list", "/api/path/v2/{id}/detail",
                        "/api/question/v2/list", "/api/question/v2/category/list", "/api/question/v2/detail/{id}", "/api/comments/v2/list",
                        "/api/category/getCategoryById", "/api/category/getCategoryList", "/api/category/getCategorySimpleList", "/api/category/getCategoryPage")
                .order(0);
    }

    // 注册 Sa-Token 全局过滤器
    @Bean
    public SaServletFilter getSaServletFilter() {
        return new SaServletFilter()
                .addInclude("/**")
                .addExclude("/favicon.ico")
                .setAuth(obj -> {
                    // 校验 Same-Token 身份凭证
                    // SaSameUtil.checkCurrentRequestToken();
                    String token = SaHolder.getRequest().getHeader(SaSameUtil.SAME_TOKEN);
                    SaSameUtil.checkToken(token);
                })
                .setError(e -> SaResult.error(e.getMessage()));
    }

}
