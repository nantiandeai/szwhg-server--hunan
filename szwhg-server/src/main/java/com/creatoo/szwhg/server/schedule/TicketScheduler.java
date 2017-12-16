package com.creatoo.szwhg.server.schedule;

import com.creatoo.szwhg.activity.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 票务定时任务
 * Created by yunyan on 2017/12/8.
 */
@Component
public class TicketScheduler {
    @Autowired
    private ActivityService activityService;

    @Scheduled(initialDelay=1000*10,fixedDelay = 1000*30)
    public void doSchedule(){
        //活动票
        activityService.publishTickets();
    }
}
