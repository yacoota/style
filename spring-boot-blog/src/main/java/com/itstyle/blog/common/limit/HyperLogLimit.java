package com.itstyle.blog.common.limit;


import java.lang.annotation.*;

/**
 * 浏览量统计
 * 创建者  爪洼笔记
 * 博客 https://blog.52itstyle.vip
 * 创建时间	2019年8月15日
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public  @interface HyperLogLimit {
    /**
     * 描述
     */
    String description()  default "";

}
