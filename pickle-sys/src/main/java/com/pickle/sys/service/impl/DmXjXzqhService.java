package com.pickle.sys.service.impl;

import com.pickle.sys.bean.DmXjXzqh;
import com.pickle.sys.mapper.DmXjXzqhMapper;
import com.pickle.sys.service.IDmXjXzqhService;
import com.pickle.utils.base.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DmXjXzqhService extends BaseService<DmXjXzqh> implements IDmXjXzqhService {
    private final DmXjXzqhMapper dmXjXzqhMapper;
}