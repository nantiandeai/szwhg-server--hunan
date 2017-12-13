package com.creatoo.szwhg.base.service;


import com.creatoo.szwhg.base.dao.StoreItemDao;
import com.creatoo.szwhg.base.model.Information;
import com.creatoo.szwhg.base.model.StoreItem;
import com.creatoo.szwhg.core.exception.BsException;
import com.creatoo.szwhg.core.util.CommonUtil;
import com.creatoo.szwhg.core.util.GeneralPredicatesBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;


/**
 * Created by wuxiangliang on 2017/4/6.
 */
@Service
public class StoreItemService {
    @Autowired
    private StoreItemDao storeItemDao;

    /**
     *  保存题库信息
     * @param storeItem
     * @return
     */
    public String saveStoreItem(StoreItem storeItem){
        storeItemDao.save(storeItem);
        return storeItem.getId();
    }

    /**
     *  删除题目
     * @param id
     */
    public void deleteById(String id){
        Assert.notNull(id,"题库Id不能为空！");
        storeItemDao.delete(id);
    }

    /**
     *  更新题目
     * @param id
     */
    public void updateStoreItem(String id,StoreItem storeItem){
        StoreItem storeItemPO = storeItemDao.findOne(id);
        if (storeItemPO == null) throw new BsException("题库不存在");
        try {
            CommonUtil.copyNullProperties(storeItem,storeItemPO);
        } catch (Exception e) {
            e.printStackTrace();
        }
        storeItemPO.setContents(storeItem.getContents());
        storeItemDao.save(storeItemPO);
    }

    /**
     *  查询单个题库
     * @param id
     * @return
     */
    public StoreItem findById(String id){
        StoreItem storeItem = storeItemDao.findOne(id);
        return storeItem;
    }

    /**
     *  查询题库列表
     * @param search
     * @return
     */
    public Page<StoreItem> findAll(String search, Pageable pageable){
        GeneralPredicatesBuilder builder=new GeneralPredicatesBuilder(Information.class);
        BooleanExpression exp=builder.build(search);
       return storeItemDao.findAll(exp,pageable);
    }
}
