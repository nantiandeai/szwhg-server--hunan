package com.creatoo.szwhg.base.dao;

import com.creatoo.szwhg.base.model.Information;
import com.creatoo.szwhg.base.model.QInformation;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

/**
 * Created by wuxiangliang on 2017/10/11.
 */
public interface InformationDao extends MongoRepository<Information, String> ,QueryDslPredicateExecutor<Information>, QuerydslBinderCustomizer<QInformation> {
    default  void customize(QuerydslBindings bindings, QInformation root) {
        bindings.bind(String.class).first((StringPath path, String value) -> path.containsIgnoreCase(value));
    }
}
