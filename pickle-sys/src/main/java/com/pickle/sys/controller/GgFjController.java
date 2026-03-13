package com.pickle.sys.controller;

import com.pickle.sys.bean.GgFj;
import com.pickle.sys.service.IGgFjService;
import com.pickle.utils.base.BaseController;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ggFj")
public class GgFjController extends BaseController<GgFj> {
    @Autowired
    private IGgFjService ggFjService;

    @RequestMapping("/save")
    public void save(@Valid @RequestBody GgFj ggFj) {
        ggFjService.save(ggFj);
    }

    @RequestMapping("/update")
    public void update(@Valid @RequestBody GgFj ggFj) {
        ggFjService.update(ggFj);
    }

    @RequestMapping("/delete")
    public void delete(@RequestBody GgFj ggFj) {
        ggFjService.deleteByPrimaryKey(ggFj.getFjUuid());
    }


}