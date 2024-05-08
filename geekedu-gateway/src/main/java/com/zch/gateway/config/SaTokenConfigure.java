package com.zch.gateway.config;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.reactor.context.SaReactorSyncHolder;
import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaHttpMethod;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import com.zch.gateway.result.SaResult;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

/**
 * 权限认证配置类
 *
 * @author Poison02
 * @date 2024/1/16
 */
@Configuration
public class SaTokenConfigure {

    @Bean
    public SaReactorFilter getSaReactorFilter() {
        return new SaReactorFilter()
                // 拦截地址
                .addInclude("/**")
                .addExclude("/favicon.ico")
                .setAuth(obj -> {
                    // 登录校验 -- 拦截所有路由，并排除 登录和验证码 接口
                    SaRouter.notMatch("/user/api/captcha/image")
                            .notMatch("/user/api/login")
                            .notMatch("/user/api/v2/login/password")
                            .notMatch("/user/api/v2/login/code")
                            .notMatch("/user/api/member/vip/list")
                            .notMatch("/user/api/getUserById")
                            .notMatch("/user/api/thumb/count/{id}")
                            .notMatch("/user/api/collection/count")
                            .notMatch("/system/api/v2/config")
                            .notMatch("/system/api/v2/navs")
                            .notMatch("/system/api/v2/links")
                            .notMatch("/system/api/v2/sliders")
                            .notMatch("/system/api/v2/announcement/{id}")
                            .notMatch("/system/api/v2/announcement")
                            .notMatch("/system/api/v2/announcement/latest")
                            .notMatch("/system/api/v2/ask/config")
                            .notMatch("/system/api/v2/eBook/recommend")
                            .notMatch("/system/api/v2/imageText/recommend")
                            .notMatch("/system/api/index/mobile")
                            .notMatch("/system/api/index/v2/block")
                            .notMatch("/system/api/search/list")
                            .notMatch("/course/api/v2/category/list")
                            .notMatch("/course/api/v2/courses")
                            .notMatch("/course/api/v2/detail/{id}")
                            .notMatch("/course/api/v2/{id}/comments")
                            .notMatch("/course/api/v2/learnedCourse")
                            .notMatch("/course/api/v2/{id}/learnedDetail")
                            .notMatch("/course/api/getCourseSimpleById/{id}")
                            .notMatch("/course/api/getCourseById/{id}")
                            .notMatch("/course/api/live/course/{id}/detail")
                            .notMatch("/course/api/v2/search")
                            .notMatch("/course/api/live/course/v2/list")
                            .notMatch("/course/api/live/course/v2/{id}/detail")
                            .notMatch("/course/api/live/course/v2/{id}/comments")
                            .notMatch("/course/api/live/course/getCourseSimpleById/{id}")
                            .notMatch("/book/api/eBook/v2/book/list")
                            .notMatch("/book/api/eBook/v2/book/{id}/detail")
                            .notMatch("/book/api/eBook/v2/book/{id}/comments")
                            .notMatch("/book/api/eBook/v2/study/list")
                            .notMatch("/book/api/imageText/v2/list")
                            .notMatch("/book/api/imageText/v2/{id}/detail")
                            .notMatch("/book/api/imageText/v2/{id}/comments")
                            .notMatch("/book/api/imageText/v2/study/list")
                            .notMatch("/book/api/path/v2/list")
                            .notMatch("/book/api/path/v2/{id}/detail")
                            .notMatch("/book/api/eBook/getEBookById/{id}")
                            .notMatch("/book/api/imageText/getImageTextById/{id}")
                            .notMatch("/book/api/path/{id}/detail")
                            .notMatch("/ask/api/question/v2/list")
                            .notMatch("/ask/api/question/v2/category/list")
                            .notMatch("/ask/api/question/v2/detail/{id}")
                            .notMatch("/ask/api/comments/v2/list")
                            .notMatch("/label/api/category/getCategoryById")
                            .notMatch("/label/api/category/getCategoryList")
                            .notMatch("/label/api/category/getCategorySimpleList")
                            .notMatch("/label/api/category/getCategoryPage")
                            .notMatch("/ws/live/course/{courseId}/video/{videoId}/token/{token}")
                            .check(r -> {
                                ServerHttpRequest request = SaReactorSyncHolder.getContext().getRequest();
                                String token = request.getHeaders().getFirst("Authorization");
                                System.out.println(token);
                                StpUtil.checkLogin();
                            });
                })
                .setError(e -> {
                    // 设置错误返回格式为JSON
                    ServerWebExchange exchange = SaReactorSyncHolder.getContext();
                    exchange.getResponse().getHeaders().set("Content-Type", "application/json: charset=utf-8");
                    return SaResult.error(e.getMessage());
                })
                // 前置函数：在每次认证函数之前执行
                .setBeforeAuth(obj -> {
                    SaHolder.getResponse()

                            // ---------- 设置跨域响应头 ----------
                            // 允许指定域访问跨域资源
                            .setHeader("Access-Control-Allow-Origin", "*")
                            // 允许所有请求方式
                            .setHeader("Access-Control-Allow-Methods", "*")
                            // 允许的header参数
                            .setHeader("Access-Control-Allow-Headers", "*")
                            // 有效时间
                            .setHeader("Access-Control-Max-Age", "3600")
                    ;

                    // 如果是预检请求，则立即返回到前端
                    SaRouter.match(SaHttpMethod.OPTIONS)
                            .free(r -> System.out.println("--------OPTIONS预检请求，不做处理"))
                            .back();
                });
    }

}
