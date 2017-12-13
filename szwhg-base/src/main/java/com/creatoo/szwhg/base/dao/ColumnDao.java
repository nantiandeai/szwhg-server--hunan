package com.creatoo.szwhg.base.dao;

import com.creatoo.szwhg.base.model.Column;
import com.creatoo.szwhg.base.model.QColumn;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import java.util.List;

/**
 * Created by wuxiangliang on 2017/10/11.
 */
public interface ColumnDao extends MongoRepository<Column, String> ,QueryDslPredicateExecutor<Column>, QuerydslBinderCustomizer<QColumn> {
    List<Column> findByParentIsNull();
    Column findByNameAndParent(String name, Column parent);
    List<Column> findByParentId(String id);
    List<Column> findByAncestors(Column column);
    default  void customize(QuerydslBindings bindings, QColumn root) {
        bindings.bind(String.class).first((StringPath path, String value) -> path.containsIgnoreCase(value));
    }
}
