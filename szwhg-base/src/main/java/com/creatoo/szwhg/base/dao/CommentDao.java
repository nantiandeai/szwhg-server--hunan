package com.creatoo.szwhg.base.dao;

import com.creatoo.szwhg.base.model.Comment;
import com.creatoo.szwhg.base.model.QComment;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

public interface CommentDao extends MongoRepository<Comment, String>,QueryDslPredicateExecutor<Comment>, QuerydslBinderCustomizer<QComment> {
    default  void customize(QuerydslBindings bindings, QComment root) {
        bindings.bind(String.class).first((StringPath path, String value) -> path.containsIgnoreCase(value));
    }
}
