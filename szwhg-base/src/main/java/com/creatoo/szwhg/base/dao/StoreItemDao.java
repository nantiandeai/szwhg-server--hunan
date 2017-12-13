package com.creatoo.szwhg.base.dao;

import com.creatoo.szwhg.base.model.QStoreItem;
import com.creatoo.szwhg.base.model.StoreItem;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

/**
 * Created by wuxiangliang on 2017/10/14.
 */
public interface StoreItemDao extends MongoRepository<StoreItem, String>,QueryDslPredicateExecutor<StoreItem>,QuerydslBinderCustomizer<QStoreItem> {
    @Override
    default  void customize(QuerydslBindings bindings, QStoreItem root) {
        bindings.bind(String.class).first((StringPath path, String value) -> path.containsIgnoreCase(value));
    }
}
