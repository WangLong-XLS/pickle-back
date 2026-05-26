package com.pickle.sys.controller;

import com.pickle.sys.bean.DmXjXzqh;
import com.pickle.sys.service.IDmXjXzqhService;
import com.pickle.utils.base.BaseController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dmXjXzqh")
@RequiredArgsConstructor
public class DmXjXzqhController extends BaseController<DmXjXzqh> {
    private final IDmXjXzqhService dmXjXzqhService;

    @RequestMapping("/save")
    public void save(@Valid @RequestBody DmXjXzqh dmXjXzqh) {
        dmXjXzqhService.save(dmXjXzqh);
    }

    @RequestMapping("/update")
    public void update(@Valid @RequestBody DmXjXzqh dmXjXzqh) {
        dmXjXzqhService.update(dmXjXzqh);
    }

    @RequestMapping("/delete")
    public void delete(@RequestBody DmXjXzqh dmXjXzqh) {
        dmXjXzqhService.deleteByPrimaryKey(dmXjXzqh.getXjXzqhDm());
    }
}