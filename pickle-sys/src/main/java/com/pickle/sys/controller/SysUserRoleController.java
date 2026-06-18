package com.pickle.sys.controller;

import com.pickle.sys.bean.SysUserRole;
import com.pickle.sys.service.ISysUserRoleService;
import com.pickle.utils.base.BaseController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sysUserRole")
@RequiredArgsConstructor
public class SysUserRoleController extends BaseController<SysUserRole> {
    private final ISysUserRoleService sysUserRoleService;

    @RequestMapping("/save")
    public void save(@Valid @RequestBody SysUserRole sysUserRole) {
        sysUserRoleService.save(sysUserRole);
    }

    @RequestMapping("/update")
    public void update(@Valid @RequestBody SysUserRole sysUserRole) {
        sysUserRoleService.update(sysUserRole);
    }

    @RequestMapping("/delete")
    public void delete(@RequestBody SysUserRole sysUserRole) {
        sysUserRoleService.deleteByPrimaryKey(sysUserRole.getUserRoleUuid());
    }
}