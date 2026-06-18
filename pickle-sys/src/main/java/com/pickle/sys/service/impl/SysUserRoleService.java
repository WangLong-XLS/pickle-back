package com.pickle.sys.service.impl;

import com.pickle.sys.bean.SysUserRole;
import com.pickle.sys.mapper.SysUserRoleMapper;
import com.pickle.sys.service.ISysUserRoleService;
import com.pickle.utils.base.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SysUserRoleService extends BaseService<SysUserRole> implements ISysUserRoleService {
    private final SysUserRoleMapper sysUserRoleMapper;
}