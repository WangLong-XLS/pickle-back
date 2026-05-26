package com.pickle.sys.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pickle.sys.bean.SysAdOrg;
import com.pickle.sys.service.ISysAdOrgService;
import com.pickle.utils.base.BaseController;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/sysAdOrg")
public class SysAdOrgController extends BaseController<SysAdOrg> {
    @Autowired
    private ISysAdOrgService sysAdOrgService;

    @RequestMapping("/save")
    public void save(@Valid @RequestBody SysAdOrg sysAdOrg) {
        sysAdOrgService.save(sysAdOrg);
    }

    @RequestMapping("/update")
    public void update(@Valid @RequestBody SysAdOrg sysAdOrg) {
        sysAdOrgService.update(sysAdOrg);
    }

    @RequestMapping("/delete")
    public void delete(@RequestBody SysAdOrg sysAdOrg) {
        if (!sysAdOrg.getOrgUuidIn().isEmpty()){
            sysAdOrgService.batchDeleteByPrimaryKey(sysAdOrg.getOrgUuidIn());
        }
    }

    @RequestMapping("/selectOrgCode2OrgNameMap")
    public Map<String, String> selectOrgCode2OrgNameMapList(@RequestBody SysAdOrg sysAdOrg) {
        return sysAdOrgService.selectOrgCode2OrgNameMapList(sysAdOrg);
    }

    @RequestMapping("/queryPageList")
    public PageInfo<SysAdOrg> queryPageList(@RequestBody SysAdOrg sysAdOrg) {
        PageHelper.startPage(sysAdOrg.getPageNum(), sysAdOrg.getPageSize());
        return sysAdOrgService.getPage(sysAdOrgService.queryPageList(sysAdOrg), sysAdOrg);
    }
}