package com.creatoo.szwhg.base.service;

import com.creatoo.szwhg.base.dao.ColumnDao;
import com.creatoo.szwhg.base.model.Column;
import com.creatoo.szwhg.core.exception.BsException;
import com.creatoo.szwhg.core.service.AbstractService;
import com.creatoo.szwhg.core.util.CommonUtil;
import com.creatoo.szwhg.core.util.GeneralPredicatesBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 栏目服务类
 * Created by wuxiangliang on 2017/10/11.
 */
@Service
public class ColumnService extends AbstractService {
    @Autowired
    private ColumnDao columnDao;

    public String createColumn(Column column){
        String columnName=column.getName();
        Column parent=column.getParent();
        if(parent!=null && StringUtils.isNoneBlank(parent.getId())){
            Column storeParent=columnDao.findOne(parent.getId());
            if(storeParent==null) throw new BsException("上级栏目不存在");
            List<Column> ancestors=storeParent.getAncestors();
            if(ancestors==null) ancestors=new ArrayList<>();
            ancestors.add(storeParent);
            column.setAncestors(ancestors);
        }else{
            parent=null;
        }
        if(columnDao.findByNameAndParent(columnName,parent)!=null) throw new BsException("存在相同栏目名称");
        columnDao.save(column);
        return column.getId();
    }

    public void deleteColumn(String id){
        Column column=columnDao.findOne(id);
        if(column==null) throw new BsException("栏目不存在！");
        columnDao.delete(column);
    }

    public void modifyColumn(String id,Column column){
        Column pColumn=columnDao.findOne(id);
        if(pColumn==null) throw new BsException("栏目不存在");
        try {
            CommonUtil.copyNullProperties(column,pColumn);
        } catch (Exception e) {
           throw new BsException("复制出错");
        }
        columnDao.save(pColumn);
    }

    public List<Column> getRootUnit(){
        return columnDao.findByParentIsNull();
    }

    public List<Column> getChildren(String parentId){
        List<Column> columns=columnDao.findByParentId(parentId);
        columns.sort((o1,o2)->o1.getSeqno()-o2.getSeqno());
        return columns;
    }

    public Column getOne(String id){
        Column column=columnDao.findOne(id);
        if(column==null) return null;
        List<Column> columns = getAllChildren(id);
        column.setChildren(columns);
        return column;
    }

    /**
     *  获得所有子栏目节点
     * @param parentId
     * @return
     */
    public List<Column> getAllChildren(String parentId){
        Column pColumn=columnDao.findOne(parentId);
        return columnDao.findByAncestors(pColumn);
    }

    public Page<Column> findAll(String search, Pageable pageable){
        GeneralPredicatesBuilder builder=new GeneralPredicatesBuilder(Column.class);
        BooleanExpression exp=builder.build(search);
//        Page<Column> columns = columnDao.findAll(exp,pageable);
//        List<Column> list = new ArrayList<>();
//        columns.forEach(column -> {
//            column.setChildren(getAllChildren(column.getId()));
//            list.add(column);
//        });
//        list.sort((o1, o2) -> o1.getSeqno()-o2.getSeqno());
//        List<Column> columnList = columnDao.findAll();
       return columnDao.findAll(exp,pageable);
    }
}
