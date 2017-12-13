package com.creatoo.szwhg.core.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by yunyan on 2017/8/21.
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {

    // Spring应用上下文环境
    private static ApplicationContext applicationContext;
    private static RedisTemplate<String,String> redisTemplate;

    /**
     * 实现ApplicationContextAware接口的回调方法，设置上下文环境
     */
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.applicationContext = applicationContext;
        SpringContextUtil.redisTemplate=applicationContext.getBean(RedisTemplate.class);
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 获取对象 这里重写了bean方法，起主要作用
     */
    public static Object getBean(String beanId) throws BeansException {
        return applicationContext.getBean(beanId);
    }

    public static <T> T getBean(Class<T> requiredType) throws BeansException {
        return applicationContext.getBean(requiredType);
    }

    public static String getSysParam(String paramName){
        return (String)redisTemplate.opsForHash().get("sysParam",paramName);
    }

}
