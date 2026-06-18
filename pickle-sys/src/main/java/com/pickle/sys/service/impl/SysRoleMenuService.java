package com.pickle.sys.service.impl;

import com.pickle.sys.bean.SysRoleMenu;
import com.pickle.sys.mapper.SysRoleMenuMapper;
import com.pickle.sys.service.ISysRoleMenuService;
import com.pickle.utils.base.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SysRoleMenuService extends BaseService<SysRoleMenu> implements ISysRoleMenuService {
    private final SysRoleMenuMapper sysRoleMenuMapper;
}