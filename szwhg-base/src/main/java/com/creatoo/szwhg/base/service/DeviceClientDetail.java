package com.creatoo.szwhg.base.service;

import com.creatoo.szwhg.base.model.DeviceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Component;

/**
 * Created by yunyan on 2017/11/29.
 */
@Component
public class DeviceClientDetail implements ClientDetailsService {
    @Autowired
    private DeviceService deviceService;

    @Override
    public ClientDetails loadClientByClientId(String s) throws ClientRegistrationException {
        DeviceClient device=deviceService.getByClientId(s);
        if(device==null) return null;
        ClientDetails clientDetails=new BaseClientDetails(device.getClinetId(),"","","","");
        return clientDetails;
    }
}
