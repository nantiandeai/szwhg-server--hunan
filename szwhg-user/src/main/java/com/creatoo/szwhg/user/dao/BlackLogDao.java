package com.creatoo.szwhg.user.dao;

import com.creatoo.szwhg.user.model.BlackLog;
import com.creatoo.szwhg.user.model.QBlackLog;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

/**
 * Created by yunyan on 2017/11/26.
 */
public interface BlackLogDao extends MongoRepository<BlackLog, String> ,QueryDslPredicateExecutor<BlackLog>, QuerydslBinderCustomizer<QBlackLog> {
    @Override
    default  void customize(QuerydslBindings bindings, QBlackLog root) {
        bindings.bind(String.class).first((StringPath path, String value) -> path.containsIgnoreCase(value));
    }
}
