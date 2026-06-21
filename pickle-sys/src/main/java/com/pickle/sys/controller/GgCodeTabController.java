package com.pickle.sys.controller;

import com.pickle.sys.bean.GgCodeTab;
import com.pickle.sys.service.IGgCodeTabService;
import com.pickle.utils.base.BaseController;
import com.pickle.utils.redis.RedisCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ggCodeTab")
@RequiredArgsConstructor
public class GgCodeTabController extends BaseController<GgCodeTab> {
    private final IGgCodeTabService ggCodeTabService;
    private final RedisCacheService redisCacheService;

    @RequestMapping("/save")
    public void save(@RequestBody GgCodeTab ggCodeTab) {
        ggCodeTabService.saveData(ggCodeTab);
    }

    @RequestMapping("/update")
    public void update(@RequestBody GgCodeTab ggCodeTab) {
        ggCodeTabService.updateData(ggCodeTab);
    }

    @RequestMapping("/delete")
    public void delete(@RequestBody GgCodeTab ggCodeTab) {
        if (!ggCodeTab.getCodeTabUuidIn().isEmpty()){
            ggCodeTabService.batchDeleteByPrimaryKey(ggCodeTab.getCodeTabUuidIn());
        }
    }

    @RequestMapping("/queryCodeInfoList")
    public List<GgCodeTab> queryCodeInfoList(@RequestBody GgCodeTab ggCodeTab) {
        return ggCodeTabService.queryCodeInfoList(ggCodeTab);
    }

    @RequestMapping("/queryCodeMapList")
    public Map<String, Map<String, String>> queryCodeMapList(@RequestBody GgCodeTab ggCodeTab) {
        return ggCodeTabService.queryCodeMapList(ggCodeTab);
    }
}