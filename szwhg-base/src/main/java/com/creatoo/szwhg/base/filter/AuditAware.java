package com.creatoo.szwhg.base.filter;

import com.creatoo.szwhg.base.model.Manager;
import com.creatoo.szwhg.base.service.ManagerService;
import com.creatoo.szwhg.core.model.AuditUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

/**
 * Created by yunyan on 2017/10/16.
 */
@Component
public class AuditAware implements AuditorAware<AuditUser> {
    @Autowired
    private ManagerService managerService;

    private static ThreadLocal<String> userId = new ThreadLocal<>();

    public static void setUserId(String userid){
        userId.set(userid);
    }

    @Override
    public AuditUser getCurrentAuditor() {
        String auditorId=userId.get();
        if(StringUtils.isNotEmpty(auditorId)){
            Manager manager=managerService.getManagerByUsername(auditorId);
            AuditUser user=new AuditUser();
            user.setUserId(auditorId);
            user.setUserName(manager.getName());
            return user;
        }
        else return null;
    }
}
