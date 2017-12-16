package com.creatoo.szwhg.train.dao;

import com.creatoo.szwhg.train.model.QTrainOrder;
import com.creatoo.szwhg.train.model.TrainOrder;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import java.util.List;

/**
 * Created by wangxl on 2017/9/5.
 */
public interface TrainOrderDao extends MongoRepository<TrainOrder, String>, QueryDslPredicateExecutor<TrainOrder>, QuerydslBinderCustomizer<QTrainOrder> {
    List<TrainOrder> findByTrainId(String trainId);

    @Override
    default void customize(QuerydslBindings bindings, QTrainOrder root) {
        bindings.bind(String.class).first((StringPath path, String value) -> path.containsIgnoreCase(value));
    }
}
