package com.creatoo.szwhg.venue.dao;

import com.creatoo.szwhg.venue.model.QRoomOrder;
import com.creatoo.szwhg.venue.model.RoomOrder;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import java.util.List;

/**
 * Created by yunyan on 2017/9/26.
 */
public interface RoomOrderDao  extends MongoRepository<RoomOrder, String>,QueryDslPredicateExecutor<RoomOrder>, QuerydslBinderCustomizer<QRoomOrder> {
    List<RoomOrder> findByUserId(String userId);
    List<RoomOrder> findByStatusOrStatus(String status1, String status2);
    List<RoomOrder> findByCode(String code);
    @Override
    default  void customize(QuerydslBindings bindings, QRoomOrder root) {
        bindings.bind(String.class).first((StringPath path, String value) -> path.containsIgnoreCase(value));
    }
}
