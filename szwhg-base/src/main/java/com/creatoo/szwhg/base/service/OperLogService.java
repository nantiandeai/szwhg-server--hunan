package com.creatoo.szwhg.base.service;

import com.creatoo.szwhg.base.dao.OperLogDao;
import com.creatoo.szwhg.base.model.OperLog;
import com.creatoo.szwhg.core.util.GeneralPredicatesBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Created by yunyan on 2017/11/30.
 */
@Service
public class OperLogService {
    @Autowired
    private OperLogDao logDao;

    public void createLog(OperLog log){
        logDao.save(log);
    }

    public Page<OperLog> findBySearch(String search, Pageable pageable){
        GeneralPredicatesBuilder builder=new GeneralPredicatesBuilder(OperLog.class);
        BooleanExpression exp=builder.build(search);
        return logDao.findAll(exp,pageable);
    }
}
