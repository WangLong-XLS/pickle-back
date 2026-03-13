package com.pickle.sys.service.impl;

import com.pickle.sys.bean.GgFj;
import com.pickle.sys.mapper.GgFjMapper;
import com.pickle.sys.service.IGgFjService;
import com.pickle.utils.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GgFjService extends BaseService<GgFj> implements IGgFjService {
    @Autowired
    private GgFjMapper ggFjMapper;
}