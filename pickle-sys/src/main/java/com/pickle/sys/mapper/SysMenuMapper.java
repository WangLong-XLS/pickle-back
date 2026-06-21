package com.pickle.sys.mapper;

import com.pickle.sys.bean.SysMenu;
import com.pickle.utils.base.BaseMapper;

import java.util.List;

public interface SysMenuMapper extends BaseMapper<SysMenu> {
    List<SysMenu> queryTreeList(SysMenu sysMenu);

    List<SysMenu> queryPageList(SysMenu sysMenu);
}