package com.pickle.procedure.service.impl;

import com.pickle.procedure.bean.WxCdCdxx;
import com.pickle.procedure.mapper.WxCdCdxxMapper;
import com.pickle.procedure.service.IWxCdCdxxService;
import com.pickle.utils.base.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WxCdCdxxService extends BaseService<WxCdCdxx> implements IWxCdCdxxService {
    private final WxCdCdxxMapper wxCdCdxxMapper;

    @Override
    public List<WxCdCdxx> selectCdList(WxCdCdxx wxCdCdxx) {
        List<WxCdCdxx> list = wxCdCdxxMapper.selectListByBean(wxCdCdxx);
        return list;
    }
}