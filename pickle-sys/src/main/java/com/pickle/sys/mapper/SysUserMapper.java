package com.pickle.sys.mapper;

import com.pickle.sys.bean.SysUser;
import com.pickle.utils.base.BaseMapper;

import java.util.List;

public interface SysUserMapper extends BaseMapper<SysUser> {
    List<SysUser> queryPageList(SysUser sysUser);
}