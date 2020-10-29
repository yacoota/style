package com.itstyle.blog.common.task;

import com.itstyle.blog.common.constant.SystemConstant;
import com.itstyle.blog.common.dynamicquery.DynamicQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * 百度收录
 * 创建者  爪洼笔记
 * 博客 https://blog.52itstyle.vip
 * 创建时间	2019年8月15日
 */
@Component
public class SitemapTask {

    @Autowired
    private DynamicQuery dynamicQuery;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${blog.url}")
    private  String blogUrl;

    //0 */1 * * * ?
    //每天23点执行一次 0 0 23 * * ?
    @Scheduled(cron = "0 0 23 * * ?")
    public void createSitemap() {
        logger.info("定时提交百度收录开始");
        StringBuffer xml = new  StringBuffer();
        xml.append("<?xml version='1.0' encoding='utf-8'?>\n");
        xml.append("<urlset>\n");
        String nativeSql = "SELECT id,create_time FROM blog";
        List<Object[]> list = dynamicQuery.query(nativeSql,new Object[]{});
        list.forEach(blog -> {
            String url = blogUrl+blog[0]+".html";
            xml.append("   <url>\n");
            xml.append("       <loc>"+url+"</loc>\n");
            xml.append("       <lastmod>"+blog[1]+"</lastmod>\n");
            xml.append("   </url>\n");
        });
        xml.append("</urlset>\n");
        saveAsFileWriter(xml.toString());
        logger.info("定时提交百度收录结束");
    }

    private static void saveAsFileWriter(String content) {
        String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
        String filePath = path + "static"+ SystemConstant.SF_FILE_SEPARATOR+"sitemap.xml";
        FileWriter fwriter = null;
        try {
            //true表示不覆盖原来的内容，而是加到文件的后面。若要覆盖原来的内容，直接省略这个参数就好
            fwriter = new FileWriter(filePath, false);
            fwriter.write(content);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                fwriter.flush();
                fwriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
