package com.creatoo.szwhg.base.service;

import com.creatoo.szwhg.base.dao.CodeDao;
import com.creatoo.szwhg.base.model.Code;
import com.creatoo.szwhg.base.model.CodeType;
import com.creatoo.szwhg.core.exception.BsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 字典管理
 * Created by yunyan on 2017/8/9.
 */
@Service
public class CodeService {
    @Autowired
    private CodeDao codeDao;

    public void saveCodes(String typeCode,List<Code> codes){
        CodeType type=codeDao.findByTypeCode(typeCode);
        if(type==null) throw new BsException("代码类型不存在");
        type.setCodes(codes);
        codeDao.save(type);
    }

    public List<Code> getTypeCodes(String typeCode){
        CodeType type=codeDao.findByTypeCode(typeCode);
        if(type==null) throw new BsException("代码类型不存在");
        List<Code> codes=type.getCodes();
        if(codes==null) return new ArrayList<>();
        codes.sort((o1,o2)->{return o1.getSeqno()-o2.getSeqno();});
        return codes;
    }

    public List<CodeType> getTypes(){
        return codeDao.findAllByOrderByTypeCode();
    }

    public String saveCodeType(CodeType codeType){
        codeDao.save(codeType);
        return codeType.getId();
    }

    public void deleteCodeType(String id){
        codeDao.delete(id);
    }

    public List<CodeType> findTypeList(String type){
        return codeDao.findByType(type);
    }
}
