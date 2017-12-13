package com.creatoo.szwhg.base.service;

import com.creatoo.szwhg.base.dao.SysParamDao;
import com.creatoo.szwhg.base.model.SysParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yunyan on 2017/8/28.
 */
@Service
@Transactional
public class SysParamService {
    private static final Logger logger = LoggerFactory.getLogger(SysParamService.class);
    @Autowired
    private SysParamDao paramDao;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    public void initSysParams(){
        Iterable<SysParam> params=paramDao.findAll();
        Map<String,String> map=new HashMap<>();
        params.forEach(po->map.put(po.getName(),po.getValue()));
        redisTemplate.opsForHash().putAll("sysParam",map); //加载系统参数到redis缓存
        logger.info("系统参数初始化完成");
    }

    public List<SysParam> getAllParams(){
        return paramDao.findAll();
    }

    public void updateSysParam(String paramName,String paramValue){
        SysParam param=paramDao.findFirstByName(paramName);
        if(param!=null) param.setValue(paramValue);
        paramDao.save(param);
        Iterable<SysParam> params=paramDao.findAll();
        Map<String,String> map=new HashMap<>();
        params.forEach(po->map.put(po.getName(),po.getValue()));
        redisTemplate.opsForHash().putAll("sysParam",map); //重新加载系统参数到redis缓存
    }
}
