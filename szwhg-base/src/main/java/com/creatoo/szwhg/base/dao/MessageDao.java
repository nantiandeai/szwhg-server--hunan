package com.creatoo.szwhg.base.dao;

import com.creatoo.szwhg.base.model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by yunyan on 2017/11/13.
 */
public interface MessageDao extends MongoRepository<Message, String> {
    Message findFirstByVerifyCodeAndReceiverOrderBySendTimeDesc(String code, String receiver);
}
