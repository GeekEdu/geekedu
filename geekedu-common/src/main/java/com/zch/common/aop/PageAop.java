package com.zch.common.aop;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zch.common.domain.query.PageQuery;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.util.ObjectUtils;

/**
 * 条件查询分页逻辑 Aop
 * @author Poison02
 * @date 2024/1/8
 */
@Configuration
@Aspect
@Slf4j
@EnableAspectJAutoProxy
public class PageAop {

    /**
     * 定义切入点
     */
    @Pointcut("@annotation(com.zch.common.annotation.Page)")
    public void annotation() {}

    /**
     * 环绕增强
     * @param joinPoint
     * @return
     */
    @Around("annotation()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 当前页码
        String pageNum = "1";
        // 每页记录数
        String pageSize = "10";
        PageQuery pageQuery = null;
        // 获取被增强方法的参数
        Object[] args = joinPoint.getArgs();
        // 循环获取方法中参数的值，赋值给 pageNum 和 pageSize
        for (Object arg : args) {
            if (arg instanceof PageQuery<?>) {
                pageQuery = (PageQuery) arg;
                pageNum = ObjectUtils.isEmpty(pageQuery.getPageNum()) ? pageNum : pageQuery.getPageNum();
                pageSize = ObjectUtils.isEmpty(pageQuery.getPageSize()) ? pageSize : pageQuery.getPageSize();
            }
        }
        Object result = null;
        try {
            // 调用分页插件传入开始页码和页面容量
            Page<Object> page = PageHelper.startPage(Integer.parseInt(pageNum), Integer.parseInt(pageSize));
            // 执行被增强的方法，不写则不执行
            result = joinPoint.proceed(args);
            // 获取并封装分页后的参数
            pageQuery.setTotal(page.getTotal());
            pageQuery.setPageNum(String.valueOf(page.getPageNum()));
            pageQuery.setPageSize(String.valueOf(page.getPageSize()));
            pageQuery.setPageCount(page.getPages());
        } catch (Exception e) {
            log.error("Page-Aop 中查询数据库异常", e);
        }
        return result;
    }

}
