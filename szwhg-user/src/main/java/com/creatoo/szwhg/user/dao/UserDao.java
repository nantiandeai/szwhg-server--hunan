package com.creatoo.szwhg.user.dao;

import com.creatoo.szwhg.user.model.AccountBind;
import com.creatoo.szwhg.user.model.QUser;
import com.creatoo.szwhg.user.model.User;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

/**
 * Created by yunyan on 2017/8/15.
 */
public interface UserDao extends MongoRepository<User, String>,QueryDslPredicateExecutor<User>, QuerydslBinderCustomizer<QUser> {

    User findFirstByMobile(String mobile);
    User findFistByBindsContains(AccountBind bind);
    @Override
    default  void customize(QuerydslBindings bindings, QUser root) {
        bindings.bind(String.class).first((StringPath path, String value) -> path.containsIgnoreCase(value));
    }
}
