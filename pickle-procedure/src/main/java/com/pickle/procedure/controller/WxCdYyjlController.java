package com.pickle.procedure.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pickle.procedure.bean.WxCdYyjl;
import com.pickle.procedure.service.IWxCdYyjlService;
import com.pickle.utils.base.BaseController;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wxCdYyjl")
public class WxCdYyjlController extends BaseController<WxCdYyjl> {
    @Autowired
    private IWxCdYyjlService wxCdYyjlService;

    @RequestMapping("/save")
    public void save(@Valid @RequestBody WxCdYyjl wxCdYyjl) {
        wxCdYyjlService.saveData(wxCdYyjl);
    }

    @RequestMapping("/update")
    public void update(@Valid @RequestBody WxCdYyjl wxCdYyjl) {
        wxCdYyjlService.updateData(wxCdYyjl);
    }

    @RequestMapping("/delete")
    public void delete(@RequestBody WxCdYyjl wxCdYyjl) {
        if (!wxCdYyjl.getYyjlUuidIn().isEmpty()) {
            wxCdYyjlService.batchDeleteByPrimaryKey(wxCdYyjl.getYyjlUuidIn());
        }
    }

    @RequestMapping("/queryPageList")
    public PageInfo<WxCdYyjl> queryPageList(@RequestBody WxCdYyjl wxCdYyjl) {
        PageHelper.startPage(wxCdYyjl.getPageNum(), wxCdYyjl.getPageSize());
        return wxCdYyjlService.getPage(wxCdYyjlService.queryPageList(wxCdYyjl), wxCdYyjl);
    }
}