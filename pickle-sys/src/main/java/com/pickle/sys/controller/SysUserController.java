package com.pickle.sys.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.pickle.sys.bean.SysUser;
import com.pickle.sys.service.ISysUserService;
import com.pickle.utils.base.BaseController;
import com.pickle.utils.exception.BizException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sysUser")
@RequiredArgsConstructor
public class SysUserController extends BaseController<SysUser> {
    private final ISysUserService sysUserService;

    @RequestMapping("/save")
    public void save(@Valid @RequestBody SysUser sysUser) {
        sysUserService.saveData(sysUser);
    }

    @RequestMapping("/update")
    public void update(@Valid @RequestBody SysUser sysUser) {
        sysUserService.updateData(sysUser);
    }

    @RequestMapping("/delete")
    public void delete(@RequestBody SysUser sysUser) {
        if (!sysUser.getUserUuidIn().isEmpty()){
            sysUserService.batchDeleteByPrimaryKey(sysUser.getUserUuidIn());
        }
    }

    @RequestMapping("/login")
    public SysUser login(@RequestBody SysUser sysUser){
        if (StringUtil.isEmpty(sysUser.getUserName())){
            throw new BizException("用户名不能为空");
        }
        if (StringUtil.isEmpty(sysUser.getUserPassword())){
            throw new BizException("密码不能为空");
        }
        return sysUserService.login(sysUser);
    }

    @RequestMapping("/queryPageList")
    public PageInfo<SysUser> queryPageList(@RequestBody SysUser sysUser) {
        PageHelper.startPage(sysUser.getPageNum(), sysUser.getPageSize());
        return sysUserService.getPage(sysUserService.queryPageList(sysUser), sysUser);
    }
}