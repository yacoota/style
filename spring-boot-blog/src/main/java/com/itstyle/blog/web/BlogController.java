package com.itstyle.blog.web;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.itstyle.blog.common.entity.Result;
import com.itstyle.blog.common.redis.RedisUtil;
import com.itstyle.blog.entity.Blog;
import com.itstyle.blog.service.IBlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 *  博文
 * 创建者 爪哇笔记
 */
@Controller
public class BlogController {

    @Autowired
    private IBlogService blogService;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 博文
     */
    @RequestMapping("{id}.shtml")
    @SentinelResource("blogView")
    public String page(@PathVariable("id") Long id, ModelMap model) {
        try{
            Blog blog = blogService.getById(id);
            String key = "blog_"+id;
            Long views = redisUtil.size(key);
            blog.setViews(views+blog.getViews());
            model.addAttribute("blog",blog);
        } catch (Throwable e) {
            return  "error/404";
        }
        return  "article";
    }
    /**
     * 博文
     */
    @RequestMapping("{id}.html")
    @SentinelResource("blogView")
    public String blog(@PathVariable("id") Long id, ModelMap model) {
        try{
            Blog blog = blogService.getById(id);
            String key = "blog_"+id;
            Long views = redisUtil.size(key);
            blog.setViews(views+blog.getViews());
            model.addAttribute("blog",blog);
        } catch (Throwable e) {
            return  "error/404";
        }
        return  "article";
    }
    /**
     * 博文
     */
    @RequestMapping("list")
    @ResponseBody
    public Result list(@RequestBody Map<String, Object> params) {
        return blogService.list(params);
    }
    /**
     * 博文
     */
    @RequestMapping("index")
    public String index() {
        return  "list";
    }

    /**
     * 博文
     */
    @RequestMapping("editor")
    public String editor() {
        return  "editor";
    }

}