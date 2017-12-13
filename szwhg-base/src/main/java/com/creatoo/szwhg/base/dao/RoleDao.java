package com.creatoo.szwhg.base.dao;

import com.creatoo.szwhg.base.model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by yunyan on 2017/8/3.
 */
public interface RoleDao  extends MongoRepository<Role, String> {
    Role findByNameAndUnitId(String name, String unitId);
    List<Role> findByUnitId(String unitId);
}
