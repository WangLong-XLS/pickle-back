package com.pickle.sys.service;

import com.pickle.sys.bean.SysUser;
import com.pickle.utils.base.IBaseService;

import java.util.List;

public interface ISysUserService extends IBaseService<SysUser> {
    void saveData(SysUser sysUser);

    void updateData(SysUser sysUser);

    SysUser login(SysUser sysUser);

    List<SysUser> queryPageList(SysUser sysUser);
}