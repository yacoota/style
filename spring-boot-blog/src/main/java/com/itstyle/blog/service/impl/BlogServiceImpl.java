package com.itstyle.blog.service.impl;

import com.itstyle.blog.common.constant.SystemConstant;
import com.itstyle.blog.common.dynamicquery.DynamicQuery;
import com.itstyle.blog.common.entity.Result;
import com.itstyle.blog.common.limit.BloomLimit;
import com.itstyle.blog.common.limit.HyperLogLimit;
import com.itstyle.blog.common.limit.ServiceLimit;
import com.itstyle.blog.common.redis.RedisUtil;
import com.itstyle.blog.common.util.CommonUtils;
import com.itstyle.blog.entity.Blog;
import com.itstyle.blog.repository.BloglRepository;
import com.itstyle.blog.service.IBlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class BlogServiceImpl implements IBlogService {

    @Autowired
    private DynamicQuery dynamicQuery;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private BloglRepository bloglRepository;

    /**
     * 执行顺序
     * 1）限流
     * 2）布隆
     * 3）计数
     * 4) 缓存
     * @param id
     * @return
     */
    @Override
    @ServiceLimit(limitType= ServiceLimit.LimitType.IP)
    @BloomLimit
    @HyperLogLimit
    @Cacheable(cacheNames ="blog")
    public Blog getById(Long id) {
        String nativeSql = "SELECT * FROM blog WHERE id=?";
        return dynamicQuery.nativeQuerySingleResult(Blog.class,nativeSql,new Object[]{id});
    }

    @Override
    @ServiceLimit(limitType= ServiceLimit.LimitType.IP)
    public Result list(Map<String, Object> params) {
        Integer pageNo = Integer.parseInt(params.get("pageNo").toString());
        pageNo = pageNo<1?1:pageNo;
        Pageable pageable = PageRequest.of(pageNo-1, SystemConstant.PAGE_SIZE);
        Page<Blog> list =  bloglRepository.findAll(pageable);
        return CommonUtils.msg(list);
    }

    /**
     * 非高计数法
     * @return
     */
//    public Blog getById() {
//        String key = IPUtils.getIpAddr()+"_blog_"+id;
//        boolean flag =  redisUtil.hasKey(key);
//        if(!flag){
//            redisUtil.set(key,key,3600);
//            String nativeSql = "UPDATE blog SET views = views+1 WHERE id=?";
//            dynamicQuery.nativeExecuteUpdate(nativeSql,new Object[]{id});
//        }
//    }
}