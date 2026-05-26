package com.pickle.sys.service.impl;

import com.pickle.sys.bean.SysRole;
import com.pickle.sys.mapper.SysRoleMapper;
import com.pickle.sys.service.ISysRoleService;
import com.pickle.utils.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysRoleService extends BaseService<SysRole> implements ISysRoleService {
    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Override
    public List<SysRole> queryPageList(SysRole sysRole) {
        return sysRoleMapper.selectListByBean(sysRole);
    }
}