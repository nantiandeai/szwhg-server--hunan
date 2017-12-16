package com.creatoo.szwhg.train.dao;

import com.creatoo.szwhg.train.model.QTrain;
import com.creatoo.szwhg.train.model.Train;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

/**
 * 培训DAO
 * Created by yunyan on 2017/8/17.
 */
public interface TrainDao extends MongoRepository<Train, String>, QueryDslPredicateExecutor<Train>, QuerydslBinderCustomizer<QTrain> {
    Train findByTitle(String title);

    @Override
    default void customize(QuerydslBindings bindings, QTrain root) {
        bindings.bind(String.class).first((StringPath path, String value) -> path.containsIgnoreCase(value));
        bindings.bind(root.title).first((StringPath path, String value)->path.like(value));
    }
}


