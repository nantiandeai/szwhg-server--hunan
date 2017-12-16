package com.creatoo.szwhg.venue.dao;

import com.creatoo.szwhg.venue.model.QVenueRoom;
import com.creatoo.szwhg.venue.model.VenueRoom;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

/**
 * Created by yunyan on 2017/8/17.
 */
public interface VenueRoomDao extends MongoRepository<VenueRoom, String> ,QueryDslPredicateExecutor<VenueRoom>, QuerydslBinderCustomizer<QVenueRoom> {
    @Override
    default  void customize(QuerydslBindings bindings, QVenueRoom root) {
        bindings.bind(String.class).first((StringPath path, String value) -> path.containsIgnoreCase(value));
    }
}
