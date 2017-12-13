package com.creatoo.szwhg.server.configure;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.lang.reflect.Method;

/**
 * Created by yunyan on 2017/10/2.
 */
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

    @Bean
    public CacheManager cacheManager(RedisTemplate<String,String> redisTemplate) {
        CacheManager cacheManager = new RedisCacheManager(redisTemplate);
        return cacheManager;
    }

    @Bean
    public RedisTemplate<String, Object> objectTemplate(RedisConnectionFactory factory){
        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<String, Object>();
        redisTemplate.setConnectionFactory(factory);
        StringRedisSerializer keySerializer=new StringRedisSerializer();
        redisTemplate.setKeySerializer(keySerializer);
        return redisTemplate;
    }


    @Bean @Primary
    public RedisTemplate<String, String> stringTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String,String> redisTemplate = new RedisTemplate<String, String>();
        redisTemplate.setConnectionFactory(factory);
        GenericJackson2JsonRedisSerializer valueSerializer = new GenericJackson2JsonRedisSerializer();
        StringRedisSerializer keySerializer=new StringRedisSerializer();
        redisTemplate.setValueSerializer(valueSerializer);
        redisTemplate.setKeySerializer(keySerializer);
        redisTemplate.setHashKeySerializer(keySerializer);
        redisTemplate.setHashValueSerializer(valueSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Override
    public KeyGenerator keyGenerator() {
        return new KeyGenerator(){
            @Override
            public Object generate(Object o, Method method, Object... objects) {
                StringBuilder sb = new StringBuilder();
                sb.append(o.getClass().getName());
                sb.append(method.getName());
                for (Object obj : objects) {
                    sb.append(obj.toString());
                }
                return sb.toString();
            }
        };
    }
}
