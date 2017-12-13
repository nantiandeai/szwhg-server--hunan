package com.creatoo.szwhg.base.dao;

import com.creatoo.szwhg.base.model.CodeType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by yunyan on 2017/8/9.
 */
public interface CodeDao extends MongoRepository<CodeType, String> {
    CodeType findByTypeCode(String code);
    List<CodeType> findAllByOrderByTypeCode();
    List<CodeType> findByType(String type);
}
