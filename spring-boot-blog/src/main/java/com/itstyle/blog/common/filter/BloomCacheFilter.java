package com.itstyle.blog.common.filter;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.itstyle.blog.common.dynamicquery.DynamicQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.DecimalFormat;
import java.util.List;

/**
 * 布隆缓存过滤器
 *
 * 缓存穿透:
 *
 * 一般的缓存系统，都是按照key值去缓存查询，如果不存在对应的value，就应该去DB中查找 。
 * 这个时候，如果请求的并发量很大，就会对后端的DB系统造成很大的压力。
 * 这就叫做缓存穿透。
 *
 * 造成的原因:
 *
 * 1.业务自身代码或数据出现问题
 * 2.一些恶意攻击、爬虫造成大量空的命中，此时会对数据库造成很大压力。
 */
@Component
public class BloomCacheFilter {

    public static BloomFilter<Integer> bloomFilter = null;

    @Autowired
    private DynamicQuery dynamicQuery;


    @PostConstruct
    public void init(){
        String nativeSql = "SELECT id FROM blog";
        List<Object> list = dynamicQuery.query(nativeSql,new Object[]{});
        bloomFilter = BloomFilter.create(Funnels.integerFunnel(), list.size());
        list.forEach(blog ->bloomFilter.put(Integer.parseInt(blog.toString())));
    }

    public static boolean mightContain(long key){
        return bloomFilter.mightContain((int)key);
    }

    public static void main(String[] args) {
        int capacity = 1000000;
        int key = 6666;
        bloomFilter = BloomFilter.create(Funnels.integerFunnel(), capacity);
        for (int i = 0; i < capacity; i++) {
            bloomFilter.put(i);
        }
        /**返回计算机最精确的时间，单位微妙 */
        long start = System.nanoTime();

        if (bloomFilter.mightContain(key)) {
            System.out.println("成功过滤到" + key);
        }
        long end = System.nanoTime();
        System.out.println("布隆过滤器消耗时间:" + (end - start));
        int sum = 0;
        for (int i = capacity + 20000; i < capacity + 30000; i++) {
            if (bloomFilter.mightContain(i)) {
                sum = sum + 1;
            }
        }
        //0.03
        DecimalFormat df=new DecimalFormat("0.00");//设置保留位数
        System.out.println("错判率为:" + df.format((float)sum/10000));
    }
}
