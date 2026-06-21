package com.pickle.sys.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pickle.sys.bean.GgCodeTypeTab;
import com.pickle.sys.service.IGgCodeTypeTabService;
import com.pickle.utils.base.BaseController;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/ggCodeTypeTab")
@RequiredArgsConstructor
public class GgCodeTypeTabController extends BaseController<GgCodeTypeTab> {
    private final IGgCodeTypeTabService ggCodeTypeTabService;

    @RequestMapping("/save")
    public void save(@javax.validation.Valid @RequestBody GgCodeTypeTab ggCodeTypeTab) {
        ggCodeTypeTabService.saveData(ggCodeTypeTab);
    }

    @RequestMapping("/update")
    public void update(@Valid @RequestBody GgCodeTypeTab ggCodeTypeTab) {
        ggCodeTypeTabService.updateData(ggCodeTypeTab);
    }

    @RequestMapping("/delete")
    public void delete(@RequestBody GgCodeTypeTab ggCodeTypeTab) {
        ggCodeTypeTabService.deleteData(ggCodeTypeTab);
    }
    @RequestMapping("/queryPageList")
    public PageInfo<GgCodeTypeTab> queryPageList(@RequestBody GgCodeTypeTab ggCodeTypeTab) {
        PageHelper.startPage(ggCodeTypeTab.getPageNum(), ggCodeTypeTab.getPageSize());
        return ggCodeTypeTabService.getPage(ggCodeTypeTabService.queryPageList(ggCodeTypeTab), ggCodeTypeTab);
    }
}