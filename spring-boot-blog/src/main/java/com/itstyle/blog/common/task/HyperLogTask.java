package com.itstyle.blog.common.task;

import com.itstyle.blog.common.dynamicquery.DynamicQuery;
import com.itstyle.blog.common.redis.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 基数统计
 * 创建者  爪洼笔记
 * 博客 https://blog.52itstyle.vip
 * 创建时间	2019年8月15日
 */
@Component
public class HyperLogTask {

    @Autowired
    private DynamicQuery dynamicQuery;

    @Autowired
    private RedisUtil redisUtil;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Scheduled(cron = "0 30 23 * * ?")
    @Transactional(rollbackFor=Exception.class)
    public void createHyperLog() {
        logger.info("计数落库开始");
        String nativeSql = "SELECT id FROM blog";
        List<Object> list = dynamicQuery.query(nativeSql,new Object[]{});
        list.forEach(blogId ->{
            String key  = "blog_"+blogId;
            Long views = redisUtil.size(key);
            if(views>0){
                String updateSql = "UPDATE blog SET views=views+? WHERE id=?";
                dynamicQuery.nativeExecuteUpdate(updateSql,new Object[]{views,blogId});
                redisUtil.del(key);
            }
        });
        logger.info("计数落库结束");
    }
}
