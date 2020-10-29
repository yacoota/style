package com.itstyle.blog.web;

import com.itstyle.blog.common.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@RestController
public class HyperLogLog {


    @Autowired
    private RedisUtil redisUtil;

    private static int corePoolSize = Runtime.getRuntime().availableProcessors();
    //创建线程池  调整队列数 拒绝服务
    private static ThreadPoolExecutor executor  = new ThreadPoolExecutor(corePoolSize, corePoolSize+1, 10l, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(100000));

    @RequestMapping("count")
    public String list() {
        int skillNum = 100000;
        final CountDownLatch latch = new CountDownLatch(skillNum);
        for(int i=0;i<skillNum;i++){
            final long userId = i;
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    System.out.println("用户:"+userId);
                    redisUtil.add("blog_1",userId);
                    latch.countDown();
                }
            };
            executor.execute(task);
        }
        try {
            latch.await();// 等待所有人任务结束
            System.out.println("并发博客访问量："+redisUtil.size("blog_1"));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "ok";
    }
}
