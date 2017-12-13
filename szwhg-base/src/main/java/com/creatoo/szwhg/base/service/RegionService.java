package com.creatoo.szwhg.base.service;

import com.creatoo.szwhg.base.dao.RegionDao;
import com.creatoo.szwhg.base.model.Region;
import com.creatoo.szwhg.core.exception.BsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RegionService {
    @Autowired
    private RegionDao regionDao;

    public String createRegion(Region region){
        Region pregion = region.getParent();
        if (pregion !=null){
            Region pr = regionDao.findOne(pregion.getId());
            if (pr == null) {
                throw new BsException("父节点不存在");
            }
            List<Region> ancestor = pr.getAncestors();
            if (ancestor == null) {
                ancestor = new ArrayList<>();
            }
            ancestor.add(pr);
            region.setAncestors(ancestor);
        }
        regionDao.save(region);
        return region.getId();
    }

    /**
     *  查出所有的子集
     * @param code
     * @return
     */
    public List<Region> getAllChilds(String code){
        Region region = regionDao.findByCode(code);
        return regionDao.findByAncestors(region);
    }

    public List<Region> getDirecChilds(String code){
        List<Region> regions = null;
        if (code.equals("root")){
            regions = regionDao.findByParentIsNull();
        }else {
            Region region = regionDao.findByCode(code);
            regions = regionDao.findByParent(region);
        }
        return regions;
    }

    public Region getRegion(String code){
        return regionDao.findByCode(code);
    }
}
