package com.creatoo.szwhg.base.dao;

import com.creatoo.szwhg.base.model.Region;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by wuxiangliang on 2017/10/24.
 */
public interface RegionDao extends MongoRepository<Region, String> {
    List<Region> findByAncestors(Region region);
    List<Region> findByParent(Region region);
    List<Region> findByParentIsNull();
    Region findByCode(String code);
}
