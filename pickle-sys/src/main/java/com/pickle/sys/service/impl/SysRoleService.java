package com.pickle.sys.service.impl;

import com.pickle.sys.bean.SysRole;
import com.pickle.sys.mapper.SysRoleMapper;
import com.pickle.sys.service.ISysRoleService;
import com.pickle.utils.base.BaseService;
import com.pickle.utils.enums.YesOrNo;
import com.pickle.utils.exception.BizException;
import com.pickle.utils.uuid.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysRoleService extends BaseService<SysRole> implements ISysRoleService {
    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Override
    public List<SysRole> queryPageList(SysRole sysRole) {
        List<SysRole> list = sysRoleMapper.selectListByBean(sysRole);
        list.forEach(e -> e.setPubFlgName(YesOrNo.YES.getCode().equals(e.getPubFlg()) ? YesOrNo.YES.getMessage() : YesOrNo.NO.getMessage()));
        return list;
    }

    @Override
    public void saveData(SysRole sysRole) {
        SysRole role = new SysRole();
        role.setRoleCode(sysRole.getRoleCode());
        if (sysRoleMapper.selectCountByBean(role) > 0){
            throw new BizException("角色编码：" +sysRole.getRoleCode() +"已存在！");
        }
        role = new SysRole();
        role.setRoleName(sysRole.getRoleName());
        if (sysRoleMapper.selectCountByBean(role) > 0){
            throw new BizException("角色名称：" +sysRole.getRoleName() +"已存在！");
        }

        sysRole.setRoleUuid(UUIDUtil.newUUID());
        sysRoleMapper.insertSelective(sysRole);
    }

    @Override
    public void updateData(SysRole sysRole) {
        SysRole role = new SysRole();
        role.setRoleCode(sysRole.getRoleCode());
        List<SysRole> codeList = sysRoleMapper.selectListByBean(role);
        codeList.forEach(e ->{
            if (!e.getRoleUuid().equals(sysRole.getRoleUuid())){
                throw new BizException("角色编码：" +sysRole.getRoleCode() +"已存在！");
            }
        });
        role = new SysRole();
        role.setRoleName(sysRole.getRoleName());
        List<SysRole> nameList = sysRoleMapper.selectListByBean(role);
        nameList.forEach(e ->{
            if (!e.getRoleUuid().equals(sysRole.getRoleUuid())){
                throw new BizException("角色名称：" +sysRole.getRoleName() +"已存在！");
            }
        });

        sysRoleMapper.updateByPrimaryKeySelective(sysRole);
    }
}