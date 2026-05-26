package com.pickle.sys.service.impl;

import com.pickle.sys.bean.SysAdOrg;
import com.pickle.sys.mapper.SysAdOrgMapper;
import com.pickle.sys.service.ISysAdOrgService;
import com.pickle.utils.base.BaseService;
import com.pickle.utils.constant.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SysAdOrgService extends BaseService<SysAdOrg> implements ISysAdOrgService {
    @Autowired
    private SysAdOrgMapper sysAdOrgMapper;

    @Override
    @Cacheable(value = Cache.ORG, key = "'ORG_MAP'")
    public Map<String, String> selectOrgCode2OrgNameMapList(SysAdOrg sysAdOrg) {
        List<SysAdOrg> list = sysAdOrgMapper.selectListByBean(new SysAdOrg());
        return list.stream().collect(Collectors.toMap(SysAdOrg::getOrgCode, SysAdOrg::getOrgName));
    }

    @Override
    public List<SysAdOrg> queryPageList(SysAdOrg sysAdOrg) {
        List<SysAdOrg> list = sysAdOrgMapper.selectListByBean(sysAdOrg);
     /*   list.forEach(e ->{
            if (e.getIsCorporate() != null){
                e.setIsCorporateName(e.getIsCorporate().equals(YesOrNo.YES.getCode()) ? YesOrNo.YES.getMessage() : YesOrNo.NO.getMessage());
            }
            if (e.getIsGroup() != null){
                e.setIsGroupName(e.getIsGroup().equals(YesOrNo.YES.getCode()) ? YesOrNo.YES.getMessage() : YesOrNo.NO.getMessage());
            }
        });*/
        return list;
    }
}