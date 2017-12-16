package com.creatoo.szwhg.venue.dao;

import com.creatoo.szwhg.venue.model.QVenue;
import com.creatoo.szwhg.venue.model.Venue;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import java.util.List;

/**
 * Created by yunyan on 2017/8/16.
 */
public interface VenueDao extends MongoRepository<Venue, String>,QueryDslPredicateExecutor<Venue>, QuerydslBinderCustomizer<QVenue> {
    List<Venue> findByUnitId(String unitId);

    @Override
    default  void customize(QuerydslBindings bindings, QVenue root) {
        bindings.bind(String.class).first((StringPath path, String value) -> path.containsIgnoreCase(value));
    }
}
