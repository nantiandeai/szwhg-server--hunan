package com.creatoo.szwhg.base.service;

import com.creatoo.szwhg.base.dao.CultureBrandDao;
import com.creatoo.szwhg.base.model.CultureBrand;
import com.creatoo.szwhg.core.exception.BsException;
import com.creatoo.szwhg.core.util.CommonUtil;
import com.creatoo.szwhg.core.util.GeneralPredicatesBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Created by lzh on 2017/11/15.
 */
@Service
public class CultureBrandService {
    @Autowired
    private CultureBrandDao brandDao;


    /**
     *  查询列表
     * @param search 查询参数
     * @param pageable 分页对象
     * @return
     */
    public Page<CultureBrand> findAll(String search, Pageable pageable) {
        GeneralPredicatesBuilder builder = new GeneralPredicatesBuilder(CultureBrand.class);
        BooleanExpression exp = builder.build(search);
        return brandDao.findAll(exp, pageable);
    }

    /**
     *  创建
     * @param brand  文化品牌对象
     * @return
     */
    public String createBrand(CultureBrand brand) {
        brandDao.save(brand);
        return brand.getId();
    }

    /**
     *  编辑
     * @param id 品牌id
     * @param brand1 文化品牌对象
     */
    public void modifyBrand(String id, CultureBrand brand1) {
        CultureBrand brand = brandDao.findOne(id);
        if (brand == null) {
            throw new BsException("品牌不存在");
        }
        try {
            CommonUtil.copyNullProperties(brand1, brand);
        } catch (Exception e) {
            throw new BsException("复制出错");
        }
        brandDao.save(brand);
    }

    /**
     * 查询详情
     * @param id 品牌id
     * @return
     */
    public CultureBrand getOne(String id) {
        return brandDao.findOne(id);
    }

    /**
     * 删除
     * @param id 品牌id
     */
    public void delete(String id) {
        brandDao.delete(id);
    }

}
