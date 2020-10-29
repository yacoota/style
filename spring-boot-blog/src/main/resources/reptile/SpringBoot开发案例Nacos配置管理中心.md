## 前言

在开发过程中，通常我们会配置一些参数来实现某些功能，比如是否开启某项服务，告警邮件配置等等。一般会通过硬编码、配置文件或者数据库的形式实现。

那么问题来了，如何更加优雅的实现？欢迎来到 Nacos 的世界！

## Nacos 配置管理

Nacos 是阿里巴巴的开源的项目，全称 Naming Configuration Service ，专注于服务发现和配置管理领域。

Nacos 致力于帮助您发现、配置和管理微服务。Nacos 提供了一组简单易用的特性集，帮助您快速实现动态服务发现、服务配置、服务元数据及流量管理。

## Nacos 生态图

![](https://blog.52itstyle.vip/201907/109211-20190722085842453-28430656.png)

如 Nacos 全景图所示，Nacos 无缝支持一些主流的开源生态，例如

  * Spring Cloud
  * Apache Dubbo and Dubbo Mesh TODO
  * Kubernetes and CNCF TODO。

使用 Nacos 简化服务发现、配置管理、服务治理及管理的解决方案，让微服务的发现、管理、共享、组合更加容易。

## Nacos Spring Boot 快速开始

这里以为 Spring-Boot2.x 为例：

pom.xml引入依赖：

    
    
    <dependency>
          <groupId>com.alibaba.boot</groupId>
          <artifactId>nacos-config-spring-boot-starter</artifactId>
          <version>0.2.1</version>
    </dependency>

启动类：

    
    
    package com.itstyle.nacos;
    
    import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.boot.SpringApplication;
    import org.springframework.boot.autoconfigure.SpringBootApplication;
    
    /**
     * 启动类
     * 创建者 爪哇笔记 https://blog.52itstyle.vip
     * 创建时间 2019年7月14日
     * dataId 可以根据自己的项目自定义
     * autoRefreshed 是一个布尔值， Nacos 就会把最新的配置推送到该应用的所有机器上，简单而高效。
     */
    @SpringBootApplication
    @NacosPropertySource(dataId = "itstyle.blog", autoRefreshed = true)
    public class Application  {
        private static final Logger logger = LoggerFactory.getLogger(Application.class);
    
        public static void main(String[] args){
            SpringApplication.run(Application.class, args);
            logger.info("启动");
        }
    

使用案例：

    
    
    package com.itstyle.nacos;
    
    import com.alibaba.nacos.api.config.annotation.NacosValue;
    import org.springframework.stereotype.Controller;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RequestMethod;
    import org.springframework.web.bind.annotation.ResponseBody;
    
    /**
     * 创建者 爪哇笔记 https://blog.52itstyle.vip
     */
    @Controller
    @RequestMapping(value = "config")
    public class NacosConfigController {
    
    
        @NacosValue(value = "${useLocalCache:false}", autoRefreshed = true)
        private boolean useLocalCache;
    
        @RequestMapping(value = "/get", method = RequestMethod.GET)
        @ResponseBody
        public boolean get() {
            return useLocalCache;
        }
    }
    

配置文件引入：

    
    
    # 安全机制，建议走内网、配置防火墙
    nacos.config.server-addr=127.0.0.1:8848

服务端安装配置请参考：

https://nacos.io/zh-cn/docs/quick-start.html

主页：

![](https://blog.52itstyle.vip/201907/109211-20190722085907641-1424567100.png)

dataId 一定要与系统配置保持一致，配置内容为键值对的方式。

![](https://blog.52itstyle.vip/201907/109211-20190722085918930-665752020.png)

## 实例化数据库

Nacos Server 默认使用的是内嵌的数据库，生产环境建议修改使用 mysql 数据库存储配置信息。

在配置文件application.properties添加配置：

    
    
    spring.datasource.platform=mysql
    db.num=1
    db.url.0=jdbc:mysql://127.0.0.1:3306/nacos?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true
    db.user=root
    db.password=root

创建数据库，在Nacos Server conf文件夹下，找到nacos-mysql.sql文件，导入创建的数据库即可。

Nacos默认账号密码为：nacos，修改密码需要使用引入：

    
    
    <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
    </dependency>

然后使用代码加密：

    
    
    package com.itstyle.nacos;
    
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    
    /**
     * 创建者 爪哇笔记 https://blog.52itstyle.vip
     */
    public class PasswordEncoderUtil {
        public static void main(String[] args) {
            System.out.println(new BCryptPasswordEncoder().encode("nacos"));
        }
    }

## 小结

总的来说，Nacos 还是蛮方便的，配置中心也仅仅是它的一个小功能而已。

## 演示案例

http://47.104.197.9:8848/nacos/

## 参考

https://nacos.io/en-us/

