package com.pickle.sys.controller;

import com.pickle.sys.bean.DmSjXzqh;
import com.pickle.sys.service.IDmSjXzqhService;
import com.pickle.utils.base.BaseController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dmSjXzqh")
@RequiredArgsConstructor
public class DmSjXzqhController extends BaseController<DmSjXzqh> {
    private final IDmSjXzqhService dmSjXzqhService;

    @RequestMapping("/save")
    public void save(@Valid @RequestBody DmSjXzqh dmSjXzqh) {
        dmSjXzqhService.save(dmSjXzqh);
    }

    @RequestMapping("/update")
    public void update(@Valid @RequestBody DmSjXzqh dmSjXzqh) {
        dmSjXzqhService.update(dmSjXzqh);
    }

    @RequestMapping("/delete")
    public void delete(@RequestBody DmSjXzqh dmSjXzqh) {
        dmSjXzqhService.deleteByPrimaryKey(dmSjXzqh.getSjXzqhDm());
    }

    @RequestMapping("/selectXzqhList")
    public List<DmSjXzqh> selectXzqhList(@RequestBody DmSjXzqh dmSjXzqh) {
        return dmSjXzqhService.selectXzqhList(dmSjXzqh);
    }
}