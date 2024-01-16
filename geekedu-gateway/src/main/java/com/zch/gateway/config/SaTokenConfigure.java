package com.zch.gateway.config;

import cn.dev33.satoken.reactor.context.SaReactorSyncHolder;
import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.ServerWebExchange;

/**
 * 权限认证配置类
 *
 * @author Poison02
 * @date 2024/1/16
 */
// @Configuration
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
                            .check(r -> StpUtil.checkLogin());
                })
                .setError(e -> {
                    // 设置错误返回格式为JSON
                    ServerWebExchange exchange = SaReactorSyncHolder.getContext();
                    exchange.getResponse().getHeaders().set("Content-Type", "application/json: charset=utf-8");
                    return SaResult.error(e.getMessage());
                });

    }

}
