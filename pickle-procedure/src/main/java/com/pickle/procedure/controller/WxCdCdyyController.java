package com.pickle.procedure.controller;

import com.pickle.procedure.bean.WxCdCdyy;
import com.pickle.procedure.service.IWxCdCdyyService;
import com.pickle.utils.base.BaseController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/wxCdCdyy")
@RequiredArgsConstructor
public class WxCdCdyyController extends BaseController<WxCdCdyy> {

    private final IWxCdCdyyService wxCdCdyyService;

    @RequestMapping("/save")
    public void save(@Valid @RequestBody WxCdCdyy wxCdCdyy) {
        wxCdCdyyService.save(wxCdCdyy);
    }

    @RequestMapping("/update")
    public void update(@Valid @RequestBody WxCdCdyy wxCdCdyy) {
        wxCdCdyyService.update(wxCdCdyy);
    }

    @RequestMapping("/delete")
    public void delete(@RequestBody WxCdCdyy wxCdCdyy) {
        wxCdCdyyService.deleteByPrimaryKey(wxCdCdyy.getCdyyUuid());
    }

    @RequestMapping("/selectMapByBean")
    public Map<String, List<WxCdCdyy>> selectMapByBean(@RequestBody WxCdCdyy wxCdCdyy) {
        return wxCdCdyyService.selectMapByBean(wxCdCdyy);
    }
}