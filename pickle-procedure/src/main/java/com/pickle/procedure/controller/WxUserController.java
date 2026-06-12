package com.pickle.procedure.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pickle.procedure.bean.WxUser;
import com.pickle.procedure.service.IWxUserService;
import com.pickle.utils.base.BaseController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/wxUser")
@RequiredArgsConstructor
public class WxUserController extends BaseController<WxUser> {
    private final IWxUserService wxUserService;

    @RequestMapping("/save")
    public void save(@Valid @RequestBody WxUser wxUser) {
        wxUserService.save(wxUser);
    }

    @RequestMapping("/update")
    public void update(@Valid @RequestBody WxUser wxUser) {
        wxUserService.updateData(wxUser);
    }

    @RequestMapping("/delete")
    public void delete(@RequestBody WxUser wxUser) {
        wxUserService.deleteByPrimaryKey(wxUser.getUserUuid());
    }

    @RequestMapping("/login")
    public WxUser login(@RequestBody WxUser sysUser){
        return wxUserService.login(sysUser);
    }

    @RequestMapping("/uploadAvatar")
    public WxUser uploadAvatar(@RequestParam("file") MultipartFile file, WxUser wxUser){
        return wxUserService.uploadAvatar(file, wxUser);
    }

    @RequestMapping("/queryPageList")
    public PageInfo<WxUser> queryPageList(@RequestBody WxUser wxUser) {
        PageHelper.startPage(wxUser.getPageNum(), wxUser.getPageSize());
        return wxUserService.getPage(wxUserService.queryPageList(wxUser), wxUser);
    }

    @RequestMapping("/selectListByBean")
    public List<WxUser> selectListByBean(@RequestBody WxUser wxUser) {
        return wxUserService.queryListByBean(wxUser);
    }
}