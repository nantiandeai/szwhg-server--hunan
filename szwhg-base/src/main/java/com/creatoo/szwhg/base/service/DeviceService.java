package com.creatoo.szwhg.base.service;

import com.creatoo.szwhg.base.dao.DeviceClientDao;
import com.creatoo.szwhg.base.model.DeviceClient;
import com.creatoo.szwhg.core.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yunyan on 2017/11/29.
 */
@Service
public class DeviceService {
    @Autowired
    private DeviceClientDao deviceDao;

    public String createDevice(DeviceClient device){
        device.setScopes(new String[]{"read","write"});
        deviceDao.save(device);
        return device.getId();
    }

    public List<DeviceClient> getDevices(){
        return deviceDao.findAll();
    }

    public void editDevice(String id,DeviceClient device){
        DeviceClient pm=deviceDao.findOne(id);
        if(pm==null) return;
        CommonUtil.copyNullProperties(device,pm);
        deviceDao.save(pm);
    }

    public DeviceClient getDevice(String id){
         return deviceDao.findOne(id);
    }

    public void deleteDevice(String id){
        deviceDao.delete(id);
    }

    public DeviceClient getByClientId(String clientid){
        return deviceDao.findFirstByClinetId(clientid);
    }
}
