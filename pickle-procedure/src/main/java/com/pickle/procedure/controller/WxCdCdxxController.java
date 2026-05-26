package com.pickle.procedure.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pickle.procedure.bean.WxCdCdxx;
import com.pickle.procedure.service.IWxCdCdxxService;
import com.pickle.utils.base.BaseController;
import com.pickle.utils.uuid.UUIDUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/wxCdCdxx")
@RequiredArgsConstructor
public class WxCdCdxxController extends BaseController<WxCdCdxx> {
    private final IWxCdCdxxService wxCdCdxxService;

    @RequestMapping("/save")
    public void save(@Valid @RequestBody WxCdCdxx wxCdCdxx) {
        wxCdCdxx.setCdxxUuid(UUIDUtil.newUUID());
        wxCdCdxxService.save(wxCdCdxx);
    }

    @RequestMapping("/update")
    public void update(@Valid @RequestBody WxCdCdxx wxCdCdxx) {
        wxCdCdxxService.update(wxCdCdxx);
    }

    @RequestMapping("/delete")
    public void delete(@RequestBody WxCdCdxx wxCdCdxx) {
        wxCdCdxxService.deleteByPrimaryKey(wxCdCdxx.getCdxxUuid());
    }

    @RequestMapping("/selectCdList")
    public List<WxCdCdxx> selectCdList(@RequestBody WxCdCdxx wxCdCdxx) {
        return wxCdCdxxService.selectCdList(wxCdCdxx);
    }

    @RequestMapping("/queryPageList")
    public PageInfo<WxCdCdxx> queryPageList(@RequestBody WxCdCdxx wxCdCdxx) {
        PageHelper.startPage(wxCdCdxx.getPageNum(), wxCdCdxx.getPageSize());
        return wxCdCdxxService.getPage(wxCdCdxxService.queryPageList(wxCdCdxx), wxCdCdxx);
    }
}