package com.pickle.sys.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pickle.sys.bean.SysRole;
import com.pickle.sys.service.ISysRoleService;
import com.pickle.utils.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sysRole")
public class SysRoleController extends BaseController<SysRole> {
    @Autowired
    private ISysRoleService sysRoleService;

    @RequestMapping("/save")
    public void save(@RequestBody SysRole sysRole) {
        sysRoleService.saveData(sysRole);
    }

    @RequestMapping("/update")
    public void update(@RequestBody SysRole sysRole) {
        sysRoleService.updateData(sysRole);
    }

    @RequestMapping("/delete")
    public void delete(@RequestBody SysRole sysRole) {
        if (!sysRole.getRoleUuidIn().isEmpty()){
            sysRoleService.batchDeleteByPrimaryKey(sysRole.getRoleUuidIn());
        }
    }


    @RequestMapping("/queryPageList")
    public PageInfo<SysRole> queryPageList(@RequestBody SysRole sysRole) {
        PageHelper.startPage(sysRole.getPageNum(), sysRole.getPageSize());
        return sysRoleService.getPage(sysRoleService.queryPageList(sysRole), sysRole);
    }

    @RequestMapping("/selectListByBean")
    public List<SysRole> selectListBean(@RequestBody SysRole sysRole) {
        return sysRoleService.queryListByBean(sysRole);
    }
}