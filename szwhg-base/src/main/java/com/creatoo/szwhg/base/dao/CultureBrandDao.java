package com.creatoo.szwhg.base.dao;


import com.creatoo.szwhg.base.model.CultureBrand;
import com.creatoo.szwhg.base.model.QCultureBrand;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

/**
 * Created by lzh on 2017/11/15.
 */
public interface CultureBrandDao extends MongoRepository<CultureBrand, String>,QueryDslPredicateExecutor<CultureBrand>, QuerydslBinderCustomizer<QCultureBrand> {
    @Override
    default  void customize(QuerydslBindings bindings, QCultureBrand root) {
        bindings.bind(String.class).first((StringPath path, String value) -> path.containsIgnoreCase(value));
    }
}
