package com.pickle.sys.service;

import com.pickle.sys.bean.SysAdOrg;
import com.pickle.utils.base.IBaseService;

import java.util.List;
import java.util.Map;

public interface ISysAdOrgService extends IBaseService<SysAdOrg> {
    Map<String, String> selectOrgCode2OrgNameMapList(SysAdOrg sysAdOrg);

    List<SysAdOrg> queryPageList(SysAdOrg sysAdOrg);
}