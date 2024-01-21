package com.zch.common.satoken.config;

import cn.dev33.satoken.dao.SaTokenDao;
import cn.dev33.satoken.jwt.StpLogicJwtForSimple;
import cn.dev33.satoken.stp.StpLogic;
import com.zch.common.satoken.dao.MySaTokenDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Poison02
 * @date 2024/1/12
 */
@Configuration
public class SaTokenConfig  {

    @Bean
    public StpLogic getStpLogicJwt() {
        // 整合 JWT 默认简单模式
        return new StpLogicJwtForSimple();
    }

    /**
     * 将 Redis 改为自定义的 不然拿不到token值
     * @return
     */
    @Bean
    public SaTokenDao saTokenDao() {
        return new MySaTokenDao();
    }

}
