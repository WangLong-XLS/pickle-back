package com.pickle.sys.service;

import com.pickle.sys.bean.SysRole;
import com.pickle.utils.base.IBaseService;

import java.util.List;

public interface ISysRoleService extends IBaseService<SysRole> {
    List<SysRole> queryPageList(SysRole sysRole);

    void saveData(SysRole sysRole);

    void updateData(SysRole sysRole);
}