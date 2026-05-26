package com.pickle.sys.controller;

import com.pickle.sys.bean.DmShijXzqh;
import com.pickle.sys.service.IDmShijXzqhService;
import com.pickle.utils.base.BaseController;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dmShijXzqh")
public class DmShijXzqhController extends BaseController<DmShijXzqh> {
    @Autowired
    private IDmShijXzqhService dmShijXzqhService;

    @RequestMapping("/save")
    public void save(@Valid @RequestBody DmShijXzqh dmShijXzqh) {
        dmShijXzqhService.save(dmShijXzqh);
    }

    @RequestMapping("/update")
    public void update(@Valid @RequestBody DmShijXzqh dmShijXzqh) {
        dmShijXzqhService.update(dmShijXzqh);
    }

    @RequestMapping("/delete")
    public void delete(@RequestBody DmShijXzqh dmShijXzqh) {
        dmShijXzqhService.deleteByPrimaryKey(dmShijXzqh.getShijXzqhDm());
    }
}