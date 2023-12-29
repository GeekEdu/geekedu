package com.zch.common.autoConfigure.swagger;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import com.zch.common.domain.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationModelsProviderPlugin;
import springfox.documentation.spi.service.contexts.RequestMappingContext;

import javax.annotation.Resource;

import static com.github.xiaoymin.knife4j.spring.util.TypeUtils.isVoid;

public class BaseSwaggerResponseModelPlugin implements OperationModelsProviderPlugin, Ordered {

    @Resource
    private TypeResolver typeResolver;

    @Override
    public boolean supports(DocumentationType documentationType) {
        return true;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE+12;
    }


    @Override
    public void apply(RequestMappingContext context) {
        ResolvedType resolvedType = isVoid(context.getReturnType()) ?
                typeResolver.resolve(Response.class) : typeResolver.resolve(Response.class, context.getReturnType());
        ResolvedType returnType = context.alternateFor(resolvedType);
        context.operationModelsBuilder().addReturn(returnType);
    }
}

