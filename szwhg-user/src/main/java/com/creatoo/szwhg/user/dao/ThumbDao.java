package com.creatoo.szwhg.user.dao;

import com.creatoo.szwhg.core.model.ResourceType;
import com.creatoo.szwhg.user.model.Thumb;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by yunyan on 2017/11/27.
 */
public interface ThumbDao  extends MongoRepository<Thumb, String> {
    Thumb findFirstByUserIdAndTypeAndObjectId(String userid, ResourceType type, String objId);
    List<Thumb> findByTypeAndObjectId(ResourceType type, String objId);
}
