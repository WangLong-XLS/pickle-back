package com.pickle.procedure.service.impl;

import com.pickle.procedure.bean.WxCdCdyy;
import com.pickle.procedure.mapper.WxCdCdyyMapper;
import com.pickle.procedure.service.IWxCdCdyyService;
import com.pickle.utils.base.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WxCdCdyyService extends BaseService<WxCdCdyy> implements IWxCdCdyyService {
    private final WxCdCdyyMapper wxCdCdyyMapper;

    @Override
    public Map<String, List<WxCdCdyy>> selectMapByBean(WxCdCdyy wxCdCdyy) {
        wxCdCdyy.setCdh("一号场");
        List<WxCdCdyy> list1 = wxCdCdyyMapper.selectMapByBean(wxCdCdyy);

        wxCdCdyy.setCdh("二号场");
        List<WxCdCdyy> list2 = wxCdCdyyMapper.selectMapByBean(wxCdCdyy);

        Map<String, List<WxCdCdyy>> map = new HashMap<>();
        map.put("list1", list1);
        map.put("list2", list2);
        return map;
    }
}