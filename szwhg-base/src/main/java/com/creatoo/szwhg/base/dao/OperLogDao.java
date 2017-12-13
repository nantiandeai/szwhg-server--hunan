package com.creatoo.szwhg.base.dao;

import com.creatoo.szwhg.base.model.OperLog;
import com.creatoo.szwhg.base.model.QOperLog;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

/**
 * Created by yunyan on 2017/11/30.
 */
public interface OperLogDao  extends MongoRepository<OperLog, String> ,QueryDslPredicateExecutor<OperLog>, QuerydslBinderCustomizer<QOperLog> {
    default  void customize(QuerydslBindings bindings, QOperLog root) {
        bindings.bind(String.class).first((StringPath path, String value) -> path.containsIgnoreCase(value));
    }
}
