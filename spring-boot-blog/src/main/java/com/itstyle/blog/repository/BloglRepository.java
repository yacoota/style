package com.itstyle.blog.repository;

import com.itstyle.blog.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BloglRepository extends JpaRepository<Blog, Long> {

}
