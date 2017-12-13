package com.creatoo.szwhg.base.dao;

import com.creatoo.szwhg.base.model.DeviceClient;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by yunyan on 2017/11/29.
 */
public interface DeviceClientDao extends MongoRepository<DeviceClient, String> {
    DeviceClient findFirstByClinetId(String clientid);
}
