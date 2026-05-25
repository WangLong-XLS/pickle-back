package com.pickle.procedure.controller;

import com.pickle.procedure.bean.WxCdCcxx;
import com.pickle.procedure.service.IWxCdCcxxService;
import com.pickle.utils.base.BaseController;
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
        wxCdCcxxService.save(wxCdCcxx);
    }

    @RequestMapping("/update")
    public void update(@Valid @RequestBody WxCdCcxx wxCdCcxx) {
        wxCdCcxxService.update(wxCdCcxx);
    }

    @RequestMapping("/delete")
    public void delete(@RequestBody WxCdCcxx wxCdCcxx) {
        wxCdCcxxService.deleteByPrimaryKey(wxCdCcxx.getCcyyUuid());
    }

    @RequestMapping("/selectCcList")
    public List<WxCdCcxx> selectCcList(@RequestBody WxCdCcxx wxCdCcxx) {
        return wxCdCcxxService.selectCcList(wxCdCcxx);
    }
}