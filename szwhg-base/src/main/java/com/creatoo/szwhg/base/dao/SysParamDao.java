package com.creatoo.szwhg.base.dao;

import com.creatoo.szwhg.base.model.SysParam;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by yunyan on 2017/8/11.
 */
public interface SysParamDao extends MongoRepository<SysParam, String> {
    SysParam findFirstByName(String name);
}
