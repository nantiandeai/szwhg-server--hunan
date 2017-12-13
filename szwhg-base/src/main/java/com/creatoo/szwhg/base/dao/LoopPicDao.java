package com.creatoo.szwhg.base.dao;

import com.creatoo.szwhg.base.model.LoopPic;
import com.creatoo.szwhg.base.model.LoopType;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by wuxiangliang on 2017/10/31.
 */
public interface LoopPicDao extends MongoRepository<LoopPic, String> {
    LoopPic findFirstByType(LoopType type);
}
