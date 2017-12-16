package com.creatoo.szwhg.user.dao;

import com.creatoo.szwhg.user.model.IdentifyApply;
import com.creatoo.szwhg.user.model.QIdentifyApply;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import java.util.List;

/**
 * Created by yunyan on 2017/9/29.
 */
public interface IdentifyApplyDao extends MongoRepository<IdentifyApply, String>,QueryDslPredicateExecutor<IdentifyApply>, QuerydslBinderCustomizer<QIdentifyApply> {

    List<IdentifyApply> findByIdnumberAndIdentifyStatus(String idnumber, String identifyStatus);
    @Override
    default  void customize(QuerydslBindings bindings, QIdentifyApply root) {
        bindings.bind(String.class).first((StringPath path, String value) -> path.containsIgnoreCase(value));
    }
}