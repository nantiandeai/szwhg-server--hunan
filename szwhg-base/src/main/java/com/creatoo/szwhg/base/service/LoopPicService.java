package com.creatoo.szwhg.base.service;

import com.creatoo.szwhg.base.dao.LoopPicDao;
import com.creatoo.szwhg.base.model.LoopContent;
import com.creatoo.szwhg.base.model.LoopPic;
import com.creatoo.szwhg.base.model.LoopType;
import com.creatoo.szwhg.core.exception.BsException;
import com.creatoo.szwhg.core.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 轮播图管理
 * Created by wuxiangliang on 2017/10/31.
 */
@Service
public class LoopPicService {
    @Autowired
    private LoopPicDao picDao;

    public String saveLoopPic(LoopPic loopPic){
       picDao.save(loopPic);
       return loopPic.getId();
    }

    public void deleteLoopPic(String id){
        picDao.delete(id);
    }

    public void modifyLoopic(String id,LoopPic loopPic){
        LoopPic pic = picDao.findOne(id);
        if (pic == null) throw new BsException("轮播图不存在");
        CommonUtil.copyNullProperties(loopPic,pic);
        picDao.save(pic);
    }

    public List<LoopPic> findAll(){
       List<LoopPic> pics = picDao.findAll();
       return pics;
    }

    public LoopPic getLoopPic(String id){
        return picDao.findOne(id);
    }

    public String saveContent(LoopType type, LoopContent content){
        LoopPic loopPic = picDao.findFirstByType(type);
        List<LoopContent> contents = loopPic.getContents();
        String cid = UUID.randomUUID().toString().replace("-","");
        content.setId(cid);
        if (contents == null){
            contents = new ArrayList<>();
            loopPic.setContents(contents);
        }
        contents.add(content);
        picDao.save(loopPic);
        return cid;
    }

    public void deleteContent(LoopType type,String cid){
        LoopPic loopPic = picDao.findFirstByType(type);
        Optional.ofNullable(loopPic).map(t->t.getContents()).orElse(new ArrayList<>()).stream().filter(c -> c.getId().equals(cid)).findFirst().ifPresent(content -> {
            loopPic.getContents().remove(content);
            picDao.save(loopPic);
        });
    }

    public List<LoopContent> getContents(LoopType type){
        LoopPic loopPic = picDao.findFirstByType(type);
        return loopPic.getContents();
    }

    public LoopContent getLoopContent(LoopType type,String cid){
        LoopPic loopPic = picDao.findFirstByType(type);
        List<LoopContent> contents = loopPic.getContents();
        LoopContent loopContent = null;
       for(LoopContent content:contents){
           if (content.getId().equals(cid)){
               loopContent = content;
               break;
           }
       }
       return loopContent;
    }

    public void modifyContent(LoopType type,String cid,LoopContent loopContent){
        LoopPic loopPic = picDao.findFirstByType(type);
        List<LoopContent> contents = loopPic.getContents();
        for(LoopContent content:contents){
            if (content.getId().equals(cid)){
               CommonUtil.copyNullProperties(loopContent,content);
                break;
            }
        }
        picDao.save(loopPic);
    }

    public void editContents(LoopType type,List<LoopContent> loopContents){
        LoopPic loopPic = picDao.findFirstByType(type);
        loopPic.setContents(loopContents);
        picDao.save(loopPic);
    }
}
