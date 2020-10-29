package com.itstyle.blog.common.limit;

import com.itstyle.blog.common.redis.RedisUtil;
import com.itstyle.blog.common.util.IPUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;


/**
 * 浏览量统计 AOP
 * 创建者  爪洼笔记
 * 博客 https://blog.52itstyle.vip
 * 创建时间	2019年8月15日
 */
@Aspect
@Configuration
@Order(3)
public class HyperLogAspect {

    @Autowired
    private RedisUtil redisUtil;

    //Service层切点  限流
    @Pointcut("@annotation(com.itstyle.blog.common.limit.HyperLogLimit)")
    public void ServiceAspect() {

    }

    @Around("ServiceAspect()")
    public  Object around(ProceedingJoinPoint joinPoint) {
        Object[] object = joinPoint.getArgs();
        Object blogId = object[0];
        Object obj = null;
        try {
            String value = IPUtils.getIpAddr();
            String key = "blog_" + blogId;
            redisUtil.add(key,value);
            obj = joinPoint.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return obj;
    }
}