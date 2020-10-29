package com.itstyle.blog.common.task;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.itstyle.blog.common.filter.BloomCacheFilter;
import com.itstyle.blog.common.dynamicquery.DynamicQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import java.util.List;

/**
 * 布隆缓存过滤器
 * 创建者  爪洼笔记
 * 博客 https://blog.52itstyle.vip
 * 创建时间	2019年8月15日
 */
@Component
public class BloomTask {

    @Autowired
    private DynamicQuery dynamicQuery;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    //每天23点执行一次
    @Scheduled(cron = "0 0 23 * * ?")
    public void createBloom() {
        logger.info("开始初始化布隆缓存过滤");
        String nativeSql = "SELECT id FROM blog";
        List<Object> list = dynamicQuery.query(nativeSql,new Object[]{});
        BloomCacheFilter.bloomFilter = BloomFilter.create(Funnels.integerFunnel(), list.size());
        list.forEach(blog ->BloomCacheFilter.bloomFilter.put(Integer.parseInt(blog.toString())));
        logger.info("初始化布隆缓存过滤结束");
    }

}
