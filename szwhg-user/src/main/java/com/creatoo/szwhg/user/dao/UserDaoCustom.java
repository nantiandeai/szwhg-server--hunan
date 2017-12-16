package com.creatoo.szwhg.user.dao;

import com.creatoo.szwhg.user.model.UserRegistDayStat;
import com.creatoo.szwhg.user.model.UserRegistMonthStat;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by yunyan on 2017/12/1.
 */
public interface UserDaoCustom {
    /**
     * 注册月统计
     * @param startMonth 开始月份，yyyyMM
     * @param endMonth 结束月份,yyyyMM
     * @return
     */
    List<UserRegistMonthStat> statRegistByMonth(String startMonth, String endMonth);

    /**
     * 注册日统计
     * @param startDay
     * @param endDay
     * @return
     */
    List<UserRegistDayStat> statRegistByDay(LocalDate startDay, LocalDate endDay);
}
