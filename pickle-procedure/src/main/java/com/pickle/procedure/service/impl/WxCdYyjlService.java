package com.pickle.procedure.service.impl;

import com.pickle.procedure.bean.WxCdYyjl;
import com.pickle.procedure.mapper.WxCdYyjlMapper;
import com.pickle.procedure.service.IWxCdYyjlService;
import com.pickle.utils.base.BaseService;
import com.pickle.utils.uuid.UUIDUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WxCdYyjlService extends BaseService<WxCdYyjl> implements IWxCdYyjlService {
    private final WxCdYyjlMapper wxCdYyjlMapper;

    @Override
    public List<WxCdYyjl> queryPageList(WxCdYyjl wxCdYyjl) {
        List<WxCdYyjl> list = wxCdYyjlMapper.selectListByBean(wxCdYyjl);
        return list;
    }

    @Override
    public void saveData(WxCdYyjl wxCdYyjl) {

        wxCdYyjl.setYyjlUuid(UUIDUtil.newUUID());
        wxCdYyjlMapper.insert(wxCdYyjl);
    }
}