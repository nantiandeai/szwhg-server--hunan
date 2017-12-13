package com.creatoo.szwhg.base.service;

import com.creatoo.szwhg.base.dao.MessageDao;
import com.creatoo.szwhg.base.model.Message;
import com.creatoo.szwhg.base.model.MsgStatus;
import com.creatoo.szwhg.core.exception.BsException;
import com.creatoo.szwhg.core.util.CommonUtil;
import com.yunpian.sdk.YunpianClient;
import com.yunpian.sdk.model.Result;
import com.yunpian.sdk.model.SmsSingleSend;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 消息服务类
 * Created by yunyan on 2017/11/13.
 */
@Service
@Transactional
@Slf4j
public class MessageService {
    @Value("${message.sms.apikey}")
    private String apikey;
    private String smsUri="https://sms.yunpian.com/v2/sms/single_send.json";
    @Value("${message.sms.company}")
    private String company;
    @Value("${message.verify.expire.minutes}")
    private int expire;
    @Autowired
    private MessageDao msgDao;

    /**
     * 发送验证码短信
     * @param receiver
     */
    public void sendVerifyCodeSms(String receiver){
        String code= CommonUtil.generateRandomCode(6);
        String content="【"+company+"】"+"您的验证码是"+code;
        boolean success=this.sendSms(content,receiver);
        if(success){
            Message msg=new Message();
            msg.setReceiver(receiver);
            msg.setContent(content);
            msg.setIsVerifyCode(true);
            msg.setVerifyCode(code);
            msg.setSendTime(LocalDateTime.now());
            msg.setStatus(MsgStatus.sent);
            msgDao.save(msg);
        }else throw new BsException("发送失败");

    }

    /**
     * 校验验证码是否正确
     * @param code
     * @param receiver
     * @return
     */
    public boolean isValidVerifyCode(String code,String receiver){
        Message msg=msgDao.findFirstByVerifyCodeAndReceiverOrderBySendTimeDesc(code,receiver);
        if(msg==null) return false;
        if(msg.getSendTime().plusMinutes(expire).isAfter(LocalDateTime.now())){
            return true;
        }
        return false;
    }

    /**
     * 发送模板短信
     * @param content
     * @param receiver
     */
    public void sendTemplateSms(String content,String receiver){
        content="【"+company+"】"+content;
        boolean success=this.sendSms(content,receiver);
        if(success){
            Message msg=new Message();
            msg.setReceiver(receiver);
            msg.setSendTime(LocalDateTime.now());
            msg.setStatus(MsgStatus.sent);
            msg.setContent(content);
            msgDao.save(msg);
        }else throw new BsException("发送失败");

    }

    private boolean sendSms(String content,String receiver){
        YunpianClient clnt = new YunpianClient(apikey).init();
        Map<String, String> param = clnt.newParam(2);
        param.put(YunpianClient.MOBILE,receiver);
        param.put(YunpianClient.TEXT, content);
        Result<SmsSingleSend> r = clnt.sms().single_send(param);
        int retCode=r.getCode();
        boolean success=false;
        if(retCode==0) success=true;
        else{
            log.error("send sms error:"+r.getDetail());
            success=false;
        }
        clnt.close();
        return success;
    }

}
