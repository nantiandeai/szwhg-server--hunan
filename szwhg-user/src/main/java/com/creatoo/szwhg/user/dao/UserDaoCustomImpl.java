package com.creatoo.szwhg.user.dao;

import com.creatoo.szwhg.user.model.UserRegistDayStat;
import com.creatoo.szwhg.user.model.UserRegistMonthStat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

/**
 * Created by yunyan on 2017/12/1.
 */
@Repository
public class UserDaoCustomImpl implements UserDaoCustom {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<UserRegistMonthStat> statRegistByMonth(String startMonth, String endMonth) {
        LocalDate firtMonth= LocalDate.parse(startMonth+"01",DateTimeFormatter.ofPattern("yyyyMMdd")); //统计开始月份
        LocalDate lastMonth=LocalDate.parse(endMonth+"01",DateTimeFormatter.ofPattern("yyyyMMdd"));  //统计结束月份
        Date startTime=Date.from(firtMonth.withDayOfMonth(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());    //时间段起点
        Date endTime=Date.from(lastMonth.with(TemporalAdjusters.lastDayOfMonth()).atTime(23,59,59)
                .atZone(ZoneId.systemDefault()).toInstant()); //时间段终点
        Aggregation agg=newAggregation(
                match(Criteria.where(("registTime")).exists(true).
                                andOperator(Criteria.where("registTime").gt(startTime),Criteria.where("registTime").lt(endTime))),//过滤
                project().andExpression("year(registTime)").as("year").
                        andExpression("month(registTime)").as("month"), //构造输出字段
                group(fields().and("year").and("month")).count().as("count")   //分组统计
        );

        AggregationResults<UserRegistMonthStat> results = mongoTemplate.aggregate(agg, "HY_USER", UserRegistMonthStat.class);
        return results.getMappedResults();
    }

    @Override
    public List<UserRegistDayStat> statRegistByDay(LocalDate startDay, LocalDate endDay) {
        Date startTime=Date.from(startDay.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());    //时间段起点
        Date endTime=Date.from(endDay.atTime(23,59,59)
                .atZone(ZoneId.systemDefault()).toInstant()); //时间段终点
        Aggregation agg=newAggregation(
                match(Criteria.where(("registTime")).exists(true).
                        andOperator(Criteria.where("registTime").gt(startTime),Criteria.where("registTime").lt(endTime))),//过滤
                project().andExpression("year(registTime)").as("year").
                        andExpression("month(registTime)").as("month").
                        andExpression("dayOfMonth(registTime)").as("day"), //构造输出字段
                group(fields().and("year").and("month").and("day")).count().as("count")   //分组统计
        );

        AggregationResults<UserRegistDayStat> results = mongoTemplate.aggregate(agg, "HY_USER", UserRegistDayStat.class);
        return results.getMappedResults();
    }
}
