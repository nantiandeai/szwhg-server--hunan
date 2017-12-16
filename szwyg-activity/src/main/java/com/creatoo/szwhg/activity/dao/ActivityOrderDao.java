package com.creatoo.szwhg.activity.dao;

import com.creatoo.szwhg.activity.model.ActOrderStatus;
import com.creatoo.szwhg.activity.model.ActivityOrder;
import com.creatoo.szwhg.activity.model.QActivityOrder;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by yunyan on 2017/9/3.
 */
public interface ActivityOrderDao extends MongoRepository<ActivityOrder, String>,QueryDslPredicateExecutor<ActivityOrder>, QuerydslBinderCustomizer<QActivityOrder> {
    ActivityOrder findFirstByUnitIdAndDrawnCodeAndItmDateGreaterThan(String unitId, String drawnCode, LocalDate date);
    List<ActivityOrder> findByActivityIdAndItmIdAndOrderStatus(String actId, String itmId, ActOrderStatus status);
    List<ActivityOrder> findByDrawnCode(String drawnCode);
    List<ActivityOrder> findByActivityIdAndItmId(String actId, String itmId);
    List<ActivityOrder> findBySeatsCode(String code);
    List<ActivityOrder> findByActivityId(String actId);
    @Override
    default  void customize(QuerydslBindings bindings, QActivityOrder root) {

    }
}