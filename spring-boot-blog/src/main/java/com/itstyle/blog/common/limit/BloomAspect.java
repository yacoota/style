package com.itstyle.blog.common.limit;

import com.itstyle.blog.common.exception.RrException;
import com.itstyle.blog.common.filter.BloomCacheFilter;
import com.itstyle.blog.common.redis.RedisUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;


/**
 * 布隆过滤器 AOP
 * 创建者  爪洼笔记
 * 博客 https://blog.52itstyle.vip
 * 创建时间	2019年8月15日
 */
@Aspect
@Configuration
@Order(2)
public class BloomAspect {

    @Autowired
    private RedisUtil redisUtil;

    //Service层切点  限流
    @Pointcut("@annotation(com.itstyle.blog.common.limit.BloomLimit)")
    public void ServiceAspect() {

    }

    @Around("ServiceAspect()")
    public  Object around(ProceedingJoinPoint joinPoint) {
        Object[] object = joinPoint.getArgs();
        Object blogId = object[0];
        Object obj;
        try {
            if(BloomCacheFilter.mightContain(Long.valueOf(blogId.toString()))){
                obj = joinPoint.proceed();
            }else{
                throw new RrException("小同志，你访问的文章不存在");
            }
        } catch (Throwable e) {
            throw new RrException("小同志，你访问的文章不存在");
        }
        return obj;
    }
}