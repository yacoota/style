package com.itstyle.blog.common.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * redis配置
 * 创建者  爪洼笔记
 * 博客 https://blog.52itstyle.vip
 * 创建时间	2019年8月15日
 */
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

	@Resource
	private LettuceConnectionFactory lettuceConnectionFactory;

	@Override
	@Bean
	public KeyGenerator keyGenerator() {
		return new KeyGenerator() {
			@Override
			public Object generate(Object target, Method method, Object... params) {
				StringBuffer sb = new StringBuffer();
				sb.append(target.getClass().getName());
				sb.append(method.getName());
				for (Object obj : params) {
					sb.append(obj.toString());
				}
				return sb.toString();
			}
		};
	}

	@Override
	@Bean
	public CacheManager cacheManager() {
		RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.RedisCacheManagerBuilder
				.fromConnectionFactory(lettuceConnectionFactory);
		@SuppressWarnings("serial")
		Set<String> cacheNames = new HashSet<String>() {
			{
				add("codeNameCache");
			}
		};
		builder.initialCacheNames(cacheNames);
		return builder.build();
	}

	/**
	 * RedisTemplate配置
	 */
	@Bean
	public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
		// 设置序列化
		Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(
				Object.class);
		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.ALL, Visibility.ANY);
		om.enableDefaultTyping(DefaultTyping.NON_FINAL);
		jackson2JsonRedisSerializer.setObjectMapper(om);
		// 配置redisTemplate
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
		redisTemplate.setConnectionFactory(lettuceConnectionFactory);
		RedisSerializer<?> stringSerializer = new StringRedisSerializer();
		// key序列化
		redisTemplate.setKeySerializer(stringSerializer);
		// value序列化
		redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
		// Hash key序列化
		redisTemplate.setHashKeySerializer(stringSerializer);
		// Hash value序列化
		redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
		redisTemplate.afterPropertiesSet();
		return redisTemplate;
	}
}
