package com.creatoo.szwhg.base.dao;

import com.creatoo.szwhg.base.model.Manager;
import com.creatoo.szwhg.base.model.QManager;
import com.creatoo.szwhg.base.model.Role;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import java.util.List;

/**
 * Created by yunyan on 2017/8/9.
 */
public interface ManagerDao extends MongoRepository<Manager, String>,QueryDslPredicateExecutor<Manager>, QuerydslBinderCustomizer<QManager> {
    Manager findFirstByUsername(String username);
    List<Manager> findByRolesContains(Role role);

    @Override
    default  void customize(QuerydslBindings bindings, QManager root) {
        bindings.bind(root.name).first((StringPath path, String value) -> path.containsIgnoreCase(value));
        bindings.excluding(root.password);
//        bindings.bind(root.unit.id).first((StringPath path, String value) -> path.eq(value));
    }
}
