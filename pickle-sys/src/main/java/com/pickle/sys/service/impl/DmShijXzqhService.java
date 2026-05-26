package com.pickle.sys.service.impl;

import com.pickle.sys.bean.DmShijXzqh;
import com.pickle.sys.mapper.DmShijXzqhMapper;
import com.pickle.sys.service.IDmShijXzqhService;
import com.pickle.utils.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DmShijXzqhService extends BaseService<DmShijXzqh> implements IDmShijXzqhService {
    @Autowired
    private DmShijXzqhMapper dmShijXzqhMapper;
}