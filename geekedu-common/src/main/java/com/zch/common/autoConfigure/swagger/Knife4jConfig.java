package com.zch.common.autoConfigure.swagger;

import cn.hutool.core.convert.ConverterRegistry;
import com.fasterxml.classmate.TypeResolver;
import com.zch.common.domain.Response;
import com.zch.common.utils.TemporalConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @author Poison02
 * @date 2023/12/28
 */
@Configuration
@ConditionalOnProperty(prefix = "geekedu.swagger", name = "enable", havingValue = "true")
@EnableConfigurationProperties(SwaggerConfigProperties.class)
public class Knife4jConfig {

    @Resource
    private SwaggerConfigProperties swaggerConfigProperties;

    @Bean(value = "defaultApi2")
    public Docket defaultApi2(TypeResolver typeResolver) {
        // 1. 初始化 Docket
        Docket docket = new Docket(DocumentationType.SWAGGER_2);
        // 2. 是否需要包装 Response
        if (swaggerConfigProperties.getEnableResponseWrap()) {
            docket.additionalModels(typeResolver.resolve(Response.class));
        }
        return docket.apiInfo(new ApiInfoBuilder()
                .title(this.swaggerConfigProperties.getTitle())
                        .description(this.swaggerConfigProperties.getDescription())
                        .contact(new Contact(
                                this.swaggerConfigProperties.getContactName(),
                                this.swaggerConfigProperties.getContactUrl(),
                                this.swaggerConfigProperties.getContactEmail()))
                        .version(this.swaggerConfigProperties.getVersion())
                        .build())
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage(swaggerConfigProperties.getPackagePath()))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    @Primary
    @ConditionalOnProperty(prefix = "geekedu.swagger", name = "enableResponseWrap",havingValue = "true")
    public BaseSwaggerResponseModelPlugin baseSwaggerResponseModelPlugin(){
        return new BaseSwaggerResponseModelPlugin();
    }
    @Bean
    @Primary
    @ConditionalOnProperty(prefix = "geekedu.swagger", name = "enableResponseWrap",havingValue = "true")
    public BaseSwaggerResponseBuilderPlugin baseSwaggerResponseBuilderPlugin(){
        return new BaseSwaggerResponseBuilderPlugin();
    }
    {
        // hutool的日期转换器加载
        ConverterRegistry converterRegistry = ConverterRegistry.getInstance();
        converterRegistry.putCustom(LocalDateTime.class, new TemporalConverter(LocalDateTime.class));
    }

}
