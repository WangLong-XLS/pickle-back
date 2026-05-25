package com.pickle.procedure.service.impl;

import com.pickle.procedure.bean.WxCdYyjl;
import com.pickle.procedure.mapper.WxCdYyjlMapper;
import com.pickle.procedure.service.IWxCdYyjlService;
import com.pickle.utils.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WxCdYyjlService extends BaseService<WxCdYyjl> implements IWxCdYyjlService {
    @Autowired
    private WxCdYyjlMapper wxCdYyjlMapper;
}