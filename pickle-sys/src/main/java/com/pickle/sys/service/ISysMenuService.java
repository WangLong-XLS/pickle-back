package com.pickle.sys.service;

import com.pickle.sys.bean.SysMenu;
import com.pickle.utils.base.IBaseService;
import jakarta.validation.Valid;

import java.util.List;

public interface ISysMenuService extends IBaseService<SysMenu> {
    List<SysMenu> getMenusByRole(@Valid SysMenu sysMenu);

    List<SysMenu> queryPageList(SysMenu sysMenu);

    List<SysMenu> selectParentList(@Valid SysMenu sysMenu);

    void saveData(@Valid SysMenu sysMenu);

    void updateData(@Valid SysMenu sysMenu);

    void deleteData(SysMenu sysMenu);
}