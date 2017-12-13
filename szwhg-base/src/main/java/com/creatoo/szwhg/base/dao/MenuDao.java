package com.creatoo.szwhg.base.dao;

import com.creatoo.szwhg.base.model.Menu;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by yunyan on 2017/8/21.
 */
public interface MenuDao extends MongoRepository<Menu, String> {
    List<Menu> findByParentIsNullOrderBySeqno();
    List<Menu> findByAncestors(Menu menu);
    List<Menu> findByCodeIn(List<String> code);
}
