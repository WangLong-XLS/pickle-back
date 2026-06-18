package com.pickle.sys.service.impl;

import com.pickle.sys.bean.SysUserOrg;
import com.pickle.sys.mapper.SysUserOrgMapper;
import com.pickle.sys.service.ISysUserOrgService;
import com.pickle.utils.base.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SysUserOrgService extends BaseService<SysUserOrg> implements ISysUserOrgService {
    private final SysUserOrgMapper sysUserOrgMapper;
}