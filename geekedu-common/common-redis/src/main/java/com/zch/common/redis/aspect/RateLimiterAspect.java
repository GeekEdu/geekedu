package com.zch.common.redis.aspect;

import cn.hutool.core.util.ArrayUtil;
import com.zch.common.redis.annotation.RateLimiter;
import com.zch.common.redis.constants.RedisConstants;
import com.zch.common.redis.enums.LimitType;
import com.zch.common.redis.utils.MessageUtils;
import com.zch.common.redis.utils.RedisUtils;
import com.zch.common.redis.utils.ServletUtils;
import com.zch.common.redis.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RateType;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;

/**
 * 限流切面处理
 * @author Poison02
 * @date 2024/3/23
 */
@Slf4j
@Aspect
public class RateLimiterAspect {

    /**
     * 定义 SPEL 表达式解析器
     */
    private final ExpressionParser parser = new SpelExpressionParser();

    /**
     * 定义 SPEL 解析模板
     */
    private final ParserContext parserContext = new TemplateParserContext();

    /**
     * 定义 SPEL 上下文对象进行解析
     */
    private final EvaluationContext context = new StandardEvaluationContext();

    /**
     * 方法参数解析器
     */
    private final ParameterNameDiscoverer pnd = new DefaultParameterNameDiscoverer();

    @Before("@annotation(rateLimiter)")
    public void doBefore(JoinPoint point, RateLimiter rateLimiter) {
        int time = rateLimiter.time();
        int count = rateLimiter.count();
        String combineKey = getCombineKey(rateLimiter, point);
        try {
            RateType rateType = RateType.OVERALL;
            if (rateLimiter.limitType() == LimitType.CLUSTER) {
                rateType = RateType.PER_CLIENT;
            }
            long number = RedisUtils.rateLimiter(combineKey, rateType, count, time);
            if (number == -1) {
                String message = rateLimiter.message();
                if (StringUtils.startWith(message, "{") && StringUtils.endWith(message, "}")) {
                    message = MessageUtils.message(org.apache.commons.lang3.StringUtils.substring(message, 1, message.length()));
                }
                throw new RuntimeException(message);
            }
            log.debug("限制令牌 => {}，剩余令牌 => {}，缓存key => {}", count, number, combineKey);
        } catch (Exception e) {
            throw new RuntimeException("服务器限流异常，请稍后再试！");
        }
    }

    public String getCombineKey(RateLimiter rateLimiter, JoinPoint point) {
        String key = rateLimiter.key();
        // 获取方法 通过方法签名来获取
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        Class<?> targetClass = method.getDeclaringClass();
        // 判断是否是 SPEL 格式
        if (StringUtils.containsAny(key, "#")) {
            // 获取参数值
            Object[] args = point.getArgs();
            // 获取方法上参数的名称
            String[] parameterNames = pnd.getParameterNames(method);
            if (ArrayUtil.isEmpty(parameterNames)) {
                throw new RuntimeException("限流key解析异常！");
            }
            for (int i = 0; i < parameterNames.length; i++) {
                context.setVariable(parameterNames[i], args);
            }
            // 解析返回给key
            try {
                Expression expression;
                if (StringUtils.startWith(key, parserContext.getExpressionPrefix())
                    && StringUtils.endWith(key, parserContext.getExpressionSuffix())) {
                    expression = parser.parseExpression(key, parserContext);
                } else {
                    expression = parser.parseExpression(key);
                }
                key = expression.getValue(context, String.class) + ":";
            } catch (Exception e) {
                throw new RuntimeException("限流key解析异常！");
            }
        }
        StringBuilder stringBuilder = new StringBuilder(RedisConstants.RATE_LIMIT_KEY);
        stringBuilder.append(ServletUtils.getRequest().getRequestURI()).append(":");
        if (rateLimiter.limitType() == LimitType.IP) {
            // 获取请求ip
            stringBuilder.append(ServletUtils.getClientIP()).append(":");
        } else if (rateLimiter.limitType() == LimitType.CLUSTER) {
            stringBuilder.append(RedisUtils.getClient().getId()).append(":");
        }
        return stringBuilder.append(key).toString();
    }

}
