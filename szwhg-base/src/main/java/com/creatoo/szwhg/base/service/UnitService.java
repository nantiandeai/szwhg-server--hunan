package com.creatoo.szwhg.base.service;

import com.creatoo.szwhg.base.dao.UnitDao;
import com.creatoo.szwhg.base.model.Unit;
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
 * 组织机构服务类
 * Created by yunyan on 2017/8/3.
 */
@Service
public class UnitService extends AbstractService {
    @Autowired
    private UnitDao unitDao;
    @Autowired
    FileService fileService;

    public String createUnit(Unit unit){
        String unitName=unit.getName();
        Unit parent=unit.getParent();
        if(parent!=null && StringUtils.isNoneBlank(parent.getId())){
            Unit storeParent=unitDao.findOne(parent.getId());
            if(storeParent==null) throw new BsException("上级组织不存在");
            List<Unit> ancestors=storeParent.getAncestors();
            if(ancestors==null) ancestors=new ArrayList<>();
            ancestors.add(storeParent);
            unit.setAncestors(ancestors);
        }else{
            parent=null;
        }
        if(unit.getType().equals(Unit.Org_Type)){
            if(unitDao.findByNameAndType(unitName,Unit.Org_Type).size()>0) throw new BsException("存在相同机构名称");
        }else if(unit.getType().equals(Unit.Dep_Type)){
            if(unitDao.findByNameAndParent(unitName,parent)!=null) throw new BsException("存在相同部门名称");
        }
        unitDao.save(unit);
        if(parent!=null ) {
            if(unit.getType().equals(Unit.Org_Type) && !parent.isHasOrgChildren()){
                parent.setHasOrgChildren(true);
            }
            if(unit.getType().equals(Unit.Dep_Type) && !parent.isHasDepChildren()){
                parent.setHasDepChildren(true);
            }
            unitDao.save(parent);
        }
        return unit.getId();
    }

    public void deleteUnit(String id){
        Unit unit=unitDao.findOne(id);
        if(unit==null) throw new BsException("组织不存在！");
        unitDao.delete(unit);
        Unit parent=unit.getParent();
        if(parent!=null){
            if(unit.getType().equals(Unit.Org_Type) && unitDao.findByParentAndType(parent,Unit.Org_Type).isEmpty()) {
                parent.setHasOrgChildren(false);
            }
            if(unit.getType().equals(Unit.Dep_Type) && unitDao.findByParentAndType(parent,Unit.Dep_Type).isEmpty()) {
                parent.setHasDepChildren(false);
            }
            unitDao.save(parent);
        }
    }

    public void modifyUnit(String id,Unit unit){
        Unit pUnit=unitDao.findOne(id);
        if(pUnit==null) throw new BsException("组织不存在");
        String unitName=unit.getName();
        unit.setHasDepChildren(pUnit.isHasDepChildren());
        unit.setHasOrgChildren(pUnit.isHasOrgChildren());
        Unit parent=unit.getParent();
        if(unitName!=null && !unitName.equals(pUnit.getName())){
            if(unitDao.findByNameAndParent(unitName,parent)!=null) throw new BsException("存在相同名称");
        }
        try {
            CommonUtil.copyNullProperties(unit,pUnit);
        } catch (Exception e) {
           throw new BsException("复制出错");
        }
        unitDao.save(pUnit);
    }

    public Unit getRootUnit(){
        Unit root=unitDao.findByParentIsNull();
        return root;
    }

    public List<Unit> getChildren(String parentId){
        List<Unit> units=unitDao.findByParentId(parentId);
        units.sort((o1,o2)->o1.getSeqno()-o2.getSeqno());
        return units;
    }

    public List<Unit> getOrgChildren(String parentId){
        List<Unit> units=unitDao.findByParentIdAndType(parentId,Unit.Org_Type);
        units.sort((o1,o2)->o1.getSeqno()-o2.getSeqno());
        return units;
    }
    public List<Unit> getDepChildren(String parentId){
        List<Unit> units=unitDao.findByParentIdAndType(parentId,Unit.Dep_Type);
        units.sort((o1,o2)->o1.getSeqno()-o2.getSeqno());
        return units;
    }

    public List<Unit> getAllDepChildren(String parentId){
        Unit pUnit=unitDao.findOne(parentId);
        return unitDao.findByAncestorsAndType(pUnit,Unit.Dep_Type);
    }

    public Page<Unit> findAll(String search, Pageable pageable){
        GeneralPredicatesBuilder builder=new GeneralPredicatesBuilder(Unit.class);
        BooleanExpression exp=builder.build(search);
        return unitDao.findAll(exp,pageable);
    }

    public Unit getOne(String id){
        Unit unit=unitDao.findOne(id);
        if(unit==null) return null;
        return unit;
    }

}
