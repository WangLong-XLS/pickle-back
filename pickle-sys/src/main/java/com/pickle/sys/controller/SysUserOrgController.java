package com.pickle.sys.controller;

import com.pickle.sys.bean.SysUserOrg;
import com.pickle.sys.service.ISysUserOrgService;
import com.pickle.utils.base.BaseController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sysUserOrg")
@RequiredArgsConstructor
public class SysUserOrgController extends BaseController<SysUserOrg> {
    private final ISysUserOrgService sysUserOrgService;

    @RequestMapping("/save")
    public void save(@Valid @RequestBody SysUserOrg sysUserOrg) {
        sysUserOrgService.save(sysUserOrg);
    }

    @RequestMapping("/update")
    public void update(@Valid @RequestBody SysUserOrg sysUserOrg) {
        sysUserOrgService.update(sysUserOrg);
    }

    @RequestMapping("/delete")
    public void delete(@RequestBody SysUserOrg sysUserOrg) {
        sysUserOrgService.deleteByPrimaryKey(sysUserOrg.getUserOrgUuid());
    }
}