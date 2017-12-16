package com.creatoo.szwhg.activity.dao;

import com.creatoo.szwhg.activity.model.Activity;
import com.creatoo.szwhg.activity.model.QActivity;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

/**
 * Created by yunyan on 2017/8/17.
 */
public interface ActivityDao extends MongoRepository<Activity, String>,QueryDslPredicateExecutor<Activity>, QuerydslBinderCustomizer<QActivity> {

    @Override
    default  void customize(QuerydslBindings bindings, QActivity root) {
        bindings.bind(String.class).first((StringPath path, String value) -> path.containsIgnoreCase(value));

    }
}
