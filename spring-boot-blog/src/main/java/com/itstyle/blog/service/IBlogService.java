package com.itstyle.blog.service;

import com.itstyle.blog.common.entity.Result;
import com.itstyle.blog.entity.Blog;

import java.util.Map;

public interface IBlogService {

    Blog getById(Long id);

    Result list(Map<String, Object> params);
}
