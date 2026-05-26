package com.pickle.procedure.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pickle.procedure.bean.WxCdCcxx;
import com.pickle.procedure.service.IWxCdCcxxService;
import com.pickle.utils.base.BaseController;
import com.pickle.utils.uuid.UUIDUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/wxCdCcxx")
@RequiredArgsConstructor
public class WxCdCcxxController extends BaseController<WxCdCcxx> {
    private final IWxCdCcxxService wxCdCcxxService;

    @RequestMapping("/save")
    public void save(@Valid @RequestBody WxCdCcxx wxCdCcxx) {
        wxCdCcxx.setCcyyUuid(UUIDUtil.newUUID());
        wxCdCcxxService.save(wxCdCcxx);
    }

    @RequestMapping("/update")
    public void update(@Valid @RequestBody WxCdCcxx wxCdCcxx) {
        wxCdCcxxService.update(wxCdCcxx);
    }

    @RequestMapping("/delete")
    public void delete(@RequestBody WxCdCcxx wxCdCcxx) {
        if (!wxCdCcxx.getCcyyUuidIn().isEmpty()){
            wxCdCcxxService.batchDeleteByPrimaryKey(wxCdCcxx.getCcyyUuidIn());
        }
    }

    @RequestMapping("/selectCcList")
    public List<WxCdCcxx> selectCcList(@RequestBody WxCdCcxx wxCdCcxx) {
        return wxCdCcxxService.selectCcList(wxCdCcxx);
    }

    @RequestMapping("/queryPageList")
    public PageInfo<WxCdCcxx> queryPageList(@RequestBody WxCdCcxx wxCdCcxx) {
        PageHelper.startPage(wxCdCcxx.getPageNum(), wxCdCcxx.getPageSize());
        return wxCdCcxxService.getPage(wxCdCcxxService.queryPageList(wxCdCcxx), wxCdCcxx);
    }
}