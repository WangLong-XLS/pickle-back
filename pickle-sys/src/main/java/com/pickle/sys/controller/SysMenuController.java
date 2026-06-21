package com.pickle.sys.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pickle.sys.bean.SysMenu;
import com.pickle.sys.service.ISysMenuService;
import com.pickle.utils.base.BaseController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sysMenu")
@RequiredArgsConstructor
public class SysMenuController extends BaseController<SysMenu> {
    private final ISysMenuService sysMenuService;

    @RequestMapping("/save")
    public void save(@Valid @RequestBody SysMenu sysMenu) {
        sysMenuService.saveData(sysMenu);
    }

    @RequestMapping("/update")
    public void update(@Valid @RequestBody SysMenu sysMenu) {
        sysMenuService.updateData(sysMenu);
    }

    @RequestMapping("/delete")
    public void delete(@RequestBody SysMenu sysMenu) {
        sysMenuService.deleteData(sysMenu);
    }

    @RequestMapping("/queryPageList")
    public PageInfo<SysMenu> queryPageList(@RequestBody SysMenu sysMenu) {
        PageHelper.startPage(sysMenu.getPageNum(), sysMenu.getPageSize());
        return sysMenuService.getPage(sysMenuService.queryPageList(sysMenu), sysMenu);
    }

    @RequestMapping("/queryTreeList")
    public List<SysMenu> queryTreeList(@Valid @RequestBody SysMenu sysMenu) {
        return sysMenuService.queryTreeList(sysMenu);
    }
}