package com.creatoo.szwhg.base.dao;

import com.creatoo.szwhg.base.model.QUnit;
import com.creatoo.szwhg.base.model.Unit;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import java.util.List;

/**
 * Created by yunyan on 2017/8/4.
 */
public interface UnitDao extends MongoRepository<Unit, String> ,QueryDslPredicateExecutor<Unit>, QuerydslBinderCustomizer<QUnit> {
    default  void customize(QuerydslBindings bindings, QUnit root) {
        bindings.bind(String.class).first((StringPath path, String value) -> path.containsIgnoreCase(value));
    }
    Unit findByParentIsNull();
    List<Unit> findByParentId(String id);
    List<Unit> findByParentIdAndType(String id, String type);
    Unit findByNameAndParent(String name, Unit parent);
    List<Unit> findByAncestorsAndType(Unit unit, String type);
    List<Unit> findByParentAndType(Unit unit, String type);
    List<Unit> findByNameAndType(String name, String type);
}
