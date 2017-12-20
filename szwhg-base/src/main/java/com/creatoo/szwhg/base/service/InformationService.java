package com.creatoo.szwhg.base.service;

import com.creatoo.szwhg.base.dao.InformationDao;
import com.creatoo.szwhg.base.model.Column;
import com.creatoo.szwhg.base.model.Information;
import com.creatoo.szwhg.base.model.QInformation;
import com.creatoo.szwhg.core.exception.BsException;
import com.creatoo.szwhg.base.model.Comment;
import com.creatoo.szwhg.base.model.CommentStatus;
import com.creatoo.szwhg.core.service.AbstractService;
import com.creatoo.szwhg.core.util.CommonUtil;
import com.creatoo.szwhg.core.util.GeneralPredicatesBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 资讯服务类
 * Created by wuxiangliang on 2017/10/11.
 */
@Service
public class InformationService extends AbstractService {
    @Autowired
    private InformationDao informationDao;
    @Autowired
    private ColumnService columnService;

    @Value("${comment.audit.isopen}")
    private Boolean auditIsopen;

    public String createInformation(Information information){
        informationDao.save(information);
        return information.getId();
    }

    public void deleteInformation(String id){
        Information information=informationDao.findOne(id);
        if(information==null) throw new BsException("资讯不存在");
        informationDao.delete(information);
    }

    public void modifyInformation(String id,Information information){
        Information information1=informationDao.findOne(id);
        if(information1==null) throw new BsException("资讯不存在");
        try {
            CommonUtil.copyNullProperties(information,information1);
        } catch (Exception e) {
           throw new BsException("复制出错");
        }
        informationDao.save(information1);
    }

    public Page<Information> findAll(String search, Pageable pageable){
        GeneralPredicatesBuilder builder=new GeneralPredicatesBuilder(Information.class);
        QInformation qInformation = QInformation.information;
        PathBuilder entityPath = new PathBuilder(Information.class, Information.class.getName()).get("column",Column.class).get("id");
        Page<Information> pages = null;
        if (search != null && search.indexOf("column.id") !=-1){
            Column column =  new Column();
            List<Column> columns = new ArrayList<>();
            StringBuilder estr = new StringBuilder();
            String[] str = search.split(",");
            BooleanExpression plusExp=null; //加上子部门的筛选条件
            for (int i=0;i<str.length;i++){
                if (str[i].indexOf("column.id") !=-1){
                    String[] clo = str[i].split(":");
                    List<Column> cls =  columnService.getAllChildren(clo[1]);
                    if (cls != null && cls.size()>0){
                       for (Column column1:cls){
                           BooleanExpression texp = entityPath.eq(column1.getId());
                           if (plusExp == null) plusExp = texp;
                           else plusExp = plusExp.or(texp);
                       }
                    }else {
                        BooleanExpression texp = entityPath.eq(clo[1]);
                        if (plusExp == null) plusExp = texp;
                    }
                }else{
                    estr.append(str[i]);
                }
            }
            BooleanExpression exp=builder.build(estr.toString());
            if(plusExp==null) plusExp=exp;
            else plusExp=plusExp.and(exp);
            pages = informationDao.findAll(plusExp,pageable);
        }else{
            BooleanExpression exp=builder.build(search);
            pages = informationDao.findAll(exp,pageable);
        }
        return pages;
    }

    public Information getOne(String id){
        Information information=informationDao.findOne(id);
        if(information==null) return null;
        return information;
    }

    public Information findById(String id){
      Information information = informationDao.findOne(id);
      return information;
    }

    public List<Column> findColumns(){
      List<Information> informations = informationDao.findAll();
        Set sets = new HashSet();
        for (Information information:informations){
            sets.addAll(information.getColumn());
        }
        List<Column> list = new ArrayList<>(sets);
        return list;
    }
}
