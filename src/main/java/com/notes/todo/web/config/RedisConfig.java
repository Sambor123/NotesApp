package com.notes.todo.web.config;

import java.lang.reflect.Method;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;
import org.springframework.session.web.http.HttpSessionStrategy;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
/**
 * 会话信息保存到redis中，启用redis缓存，头信息保存
 * 
 * @author Xiongbo
 *
 */
@Configuration
@EnableCaching
@EnableRedisHttpSession
public class RedisConfig  extends CachingConfigurerSupport{
	@Bean
    public JedisConnectionFactory connectionFactory() {
            return new JedisConnectionFactory(); 
    }
	@Bean  
    public KeyGenerator wiselyKeyGenerator(){  
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {  
                StringBuilder sb = new StringBuilder();  
                sb.append(target.getClass().getName());  
                sb.append(method.getName());
                for (Object obj : params) { 
                    sb.append(obj.toString());  
                }
                return sb.toString();  
            }  
        };  
  
    }  
    @Bean  
    public CacheManager cacheManager(
            @SuppressWarnings("rawtypes") RedisTemplate redisTemplate) {  
        return new RedisCacheManager(redisTemplate);  
    }
    
	 /**
	 * HttpSessionStrategy
	 *
	 * @return
	 */
	 @Bean
	 public HttpSessionStrategy httpSessionStrategy() {
		 return new HeaderHttpSessionStrategy();
	 }
 }
