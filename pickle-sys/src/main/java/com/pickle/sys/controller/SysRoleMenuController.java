package com.pickle.sys.controller;

import com.pickle.sys.bean.SysRoleMenu;
import com.pickle.sys.service.ISysRoleMenuService;
import com.pickle.utils.base.BaseController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sysRoleMenu")
@RequiredArgsConstructor
public class SysRoleMenuController extends BaseController<SysRoleMenu> {
    private final ISysRoleMenuService sysRoleMenuService;

    @RequestMapping("/save")
    public void save(@Valid @RequestBody SysRoleMenu sysRoleMenu) {
        sysRoleMenuService.save(sysRoleMenu);
    }

    @RequestMapping("/update")
    public void update(@Valid @RequestBody SysRoleMenu sysRoleMenu) {
        sysRoleMenuService.update(sysRoleMenu);
    }

    @RequestMapping("/delete")
    public void delete(@RequestBody SysRoleMenu sysRoleMenu) {
        sysRoleMenuService.deleteByPrimaryKey(sysRoleMenu.getRoleMenuUuid());
    }
}