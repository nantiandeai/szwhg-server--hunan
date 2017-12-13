package com.creatoo.szwhg.core.util;

import com.querydsl.core.types.dsl.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class GeneralPredicate<T> {
    private static final Logger logger = LoggerFactory.getLogger(GeneralPredicate.class);

    private Class<T> pClass;
    private SearchCriteria criteria;

    public GeneralPredicate(Class<T> pClass, final SearchCriteria criteria) {
        this.pClass=pClass;
        this.criteria = criteria;
    }

    public BooleanExpression getPredicate() {
        PathBuilder entityPath = new PathBuilder(pClass, pClass.getName());
        String key=criteria.getKey();
        Class keyType=null;  //属性的类型
        int index=key.indexOf(".");
        if(index!=-1){ //嵌入对象类型的处理
            String firtkey=key.substring(0,index);
            try {
                if(pClass.getDeclaredField(firtkey).getAnnotation(DBRef.class)!=null){ //关联对象的查询需要特殊处理
                    entityPath=entityPath.get(firtkey,pClass.getDeclaredField(firtkey).getType());
                    key=key.substring(index+1);
                    keyType=String.class;
                }else{
                    String[] fields=StringUtils.split(key,".");
                    for(String field:fields){ //普通嵌入对象需要递归获取类型
                        keyType=pClass.getDeclaredField(field).getType();
                    }
                }
            } catch (NoSuchFieldException e) {
                logger.error(e.getMessage());
            }
        }else{ //基本属性处理
            try {
                keyType=pClass.getDeclaredField(key).getType();
            } catch (NoSuchFieldException e) {
                logger.error(e.getMessage());
            }
        }
        if(keyType==null) keyType=String.class;
        Object keyvalue=criteria.getValue();
        if(keyType==Integer.class || keyType==Double.class || keyType==Float.class){
            final NumberPath path = entityPath.getNumber(key, keyType);
            Double _keyvalue=Double.parseDouble(keyvalue.toString());
            switch (criteria.getOperation()) {
                case ":":
                    return path.eq(_keyvalue);
                case ">":
                    return path.goe(_keyvalue);
                case "<":
                    return path.loe(_keyvalue);
            }
        }else if(keyType== LocalDateTime.class){
            DateTimePath path=entityPath.getDateTime(key,keyType);
            LocalDateTime _keyvalue=LocalDateTime.parse(keyvalue.toString(), DateTimeFormatter.ofPattern(CommonUtil.DateTime_Format));
            switch (criteria.getOperation()) {
                case ":":
                    return path.eq(_keyvalue);
                case ">":
                    return path.after(_keyvalue);
                case "<":
                    return path.before(_keyvalue);
            }
        }else if(keyType== LocalDate.class ){
            DatePath path=entityPath.getDate(key,keyType);
            LocalDate _keyvalue=LocalDate.parse(keyvalue.toString(), DateTimeFormatter.ofPattern(CommonUtil.Date_Format));
            switch (criteria.getOperation()) {
                case ":":
                    return path.eq(_keyvalue);
                case ">":
                    return path.after((_keyvalue));
                case "<":
                    return path.before(_keyvalue);
            }
        }else if(keyType== LocalTime.class){
            TimePath path=entityPath.getTime(key,keyType);
            LocalTime _keyvalue=LocalTime.parse(keyvalue.toString(), DateTimeFormatter.ofPattern(CommonUtil.Time_Format));
            switch (criteria.getOperation()) {
                case ":":
                    return path.eq(_keyvalue);
                case ">":
                    return path.after(_keyvalue);
                case "<":
                    return path.before(_keyvalue);
            }
        }else if(keyType == Boolean.class){
           BooleanPath path= entityPath.getBoolean(key);
           Boolean _keyvalue = Boolean.parseBoolean(keyvalue.toString());
            switch (criteria.getOperation()) {
                case ":":
                    return path.eq(_keyvalue);
            }
        }else{
            String[] values=StringUtils.split(keyvalue.toString(),"~");
            BooleanExpression el=null;
            for(String value:values){
                if(el==null) el=entityPath.get(key).eq(value);
                else el=el.or(entityPath.get(key).eq(value));
            }
            return el;
        }
        return null;
    }


    public SearchCriteria getCriteria() {
        return criteria;
    }

    public void setCriteria(final SearchCriteria criteria) {
        this.criteria = criteria;
    }


}
