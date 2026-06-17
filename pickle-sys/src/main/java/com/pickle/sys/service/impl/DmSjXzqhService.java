package com.pickle.sys.service.impl;

import com.pickle.sys.bean.DmShijXzqh;
import com.pickle.sys.bean.DmSjXzqh;
import com.pickle.sys.bean.DmXjXzqh;
import com.pickle.sys.mapper.DmShijXzqhMapper;
import com.pickle.sys.mapper.DmSjXzqhMapper;
import com.pickle.sys.mapper.DmXjXzqhMapper;
import com.pickle.sys.service.IDmSjXzqhService;
import com.pickle.utils.base.BaseService;
import com.pickle.utils.constant.Cache;
import com.pickle.utils.constant.CacheKey;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DmSjXzqhService extends BaseService<DmSjXzqh> implements IDmSjXzqhService {
    private final DmSjXzqhMapper dmSjXzqhMapper;
    private final DmShijXzqhMapper dmShijXzqhMapper;
    private final DmXjXzqhMapper dmXjXzqhMapper;

    @Override
    @Cacheable(value = Cache.DM, key = "'" + CacheKey.DM_NAME + "XZQX'")
    public List<DmSjXzqh> selectXzqhList(DmSjXzqh dmSjXzqh) {
        List<DmSjXzqh> dmSjXzqhList = dmSjXzqhMapper.selectListByBean(dmSjXzqh);
        List<DmShijXzqh> dmShijXzqhList = dmShijXzqhMapper.selectListByBean(new DmShijXzqh());
        List<DmXjXzqh> dmXjXzqhList = dmXjXzqhMapper.selectListByBean(new DmXjXzqh());


        // 构建树形结构
        return buildXzqhTree(dmSjXzqhList, dmShijXzqhList, dmXjXzqhList);
    }

    /**
     * 构建行政区划树形结构
     */
    private List<DmSjXzqh> buildXzqhTree(List<DmSjXzqh> provinceList,
                                         List<DmShijXzqh> cityList,
                                         List<DmXjXzqh> countyList) {

        // 构建县级数据Map，按市级代码分组
        Map<String, List<DmXjXzqh>> countyMap = countyList.stream().collect(Collectors.groupingBy(DmXjXzqh::getShijXzqh));

        // 构建市级数据Map，按省级代码分组，并设置对应的县级数据
        Map<String, List<DmShijXzqh>> cityMap = cityList.stream().peek(city -> {
                    // 为每个市级设置对应的县级列表
                    city.setDmXjXzqhList(countyMap.get(city.getShijXzqhDm()));
                })
                .collect(Collectors.groupingBy(DmShijXzqh::getSjXzqh));

        // 为每个省级设置对应的市级列表
        provinceList.forEach(province -> province.setDmShijXzqhList(cityMap.get(province.getSjXzqhDm())));

        return provinceList;
    }
}