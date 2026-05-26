package com.pickle.procedure.service.impl;

import com.pickle.procedure.bean.WxCdCcxx;
import com.pickle.procedure.mapper.WxCdCcxxMapper;
import com.pickle.procedure.service.IWxCdCcxxService;
import com.pickle.utils.base.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WxCdCcxxService extends BaseService<WxCdCcxx> implements IWxCdCcxxService {
    private final WxCdCcxxMapper wxCdCcxxMapper;

    @Override
    public List<WxCdCcxx> selectCcList(WxCdCcxx wxCdCcxx) {
        List<WxCdCcxx> list = wxCdCcxxMapper.selectCcList(wxCdCcxx);
        return list;
    }

    @Override
    public List<WxCdCcxx> queryPageList(WxCdCcxx wxCdCcxx) {
        List<WxCdCcxx> list = wxCdCcxxMapper.selectCcList(wxCdCcxx);
        return list;
    }
}