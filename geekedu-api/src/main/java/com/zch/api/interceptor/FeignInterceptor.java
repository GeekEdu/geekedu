package com.zch.api.interceptor;

/**
 * @author Poison02
 * @date 2024/2/7
 */

import cn.dev33.satoken.same.SaSameUtil;
import cn.dev33.satoken.stp.StpUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * feign拦截器, 在feign请求发出之前，加入一些操作
 */
@Component
public class FeignInterceptor implements RequestInterceptor {
    // 为 Feign 的 RPC 调用 添加请求头Same-Token
    @Override
    public void apply(RequestTemplate requestTemplate) {
        String token = SaSameUtil.getToken();
        requestTemplate.header(SaSameUtil.SAME_TOKEN, SaSameUtil.getToken());
        try {
            // 当存在web上下文的时候，就正常请求里面塞进去登录的时候申请到的token
            requestTemplate.header("Authorization", "Bearer " + StpUtil.getTokenValue());
        } catch (Exception e) {
            // 如果无法拿到web上下文，则token的值和satoken一致
            requestTemplate.header("Authorization", SaSameUtil.getToken());
        }
    }

    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;

    @Bean
    public Encoder feignFormEncoder() {
        return new SpringFormEncoder(new SpringEncoder(messageConverters));
    }
}

