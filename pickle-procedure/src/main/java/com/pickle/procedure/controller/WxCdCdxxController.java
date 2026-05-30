package com.pickle.procedure.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pickle.procedure.bean.WxCdCdxx;
import com.pickle.procedure.bean.entity.WxCdCdxxEntity;
import com.pickle.procedure.service.IWxCdCdxxService;
import com.pickle.utils.base.BaseController;
import com.pickle.utils.excel.ExcelUtils;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/wxCdCdxx")
@RequiredArgsConstructor
public class WxCdCdxxController extends BaseController<WxCdCdxx> {
    private final IWxCdCdxxService wxCdCdxxService;

    @RequestMapping("/save")
    public void save(@Valid @RequestBody WxCdCdxx wxCdCdxx) {
        wxCdCdxxService.saveData(wxCdCdxx);
    }

    @RequestMapping("/update")
    public void update(@Valid @RequestBody WxCdCdxx wxCdCdxx) {
        wxCdCdxxService.updateData(wxCdCdxx);
    }

    @RequestMapping("/delete")
    public void delete(@RequestBody WxCdCdxx wxCdCdxx) {
        if (!wxCdCdxx.getCdxxUuidIn().isEmpty()){
            wxCdCdxxService.batchDeleteByPrimaryKey(wxCdCdxx.getCdxxUuidIn());
        }
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

    @RequestMapping("/exportExcel")
    public void exportExcel(@RequestBody WxCdCdxx wxCdCdxx, HttpServletResponse response) {
        PageHelper.startPage(wxCdCdxx.getPageNum(), wxCdCdxx.getPageSize());
        List<WxCdCdxx> dataList = wxCdCdxxService.queryPageList(wxCdCdxx);
        List<String> fieldNames = Arrays.asList("cdMc", "cdGdRs", "bz");
        ExcelUtils.easyExcelExport(response, "场地信息", dataList, WxCdCdxxEntity.class, fieldNames);
    }

    @RequestMapping("/importExcel")
    public void importExcel(@RequestParam("file") MultipartFile file) {
        wxCdCdxxService.importExcel(file);
    }

    @RequestMapping("/selectListByBean")
    public List<WxCdCdxx> selectListBean(@RequestBody WxCdCdxx wxCdCdxx) {
        return wxCdCdxxService.queryListByBean(wxCdCdxx);
    }
}