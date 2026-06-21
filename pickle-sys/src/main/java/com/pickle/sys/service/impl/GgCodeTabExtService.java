package com.pickle.sys.service.impl;

import com.pickle.sys.bean.GgCodeTab;
import com.pickle.sys.service.IGgCodeTabExtService;
import com.pickle.utils.base.BaseService;
import com.pickle.utils.constant.Cache;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;


//在同一个类里调Cacheable方法不会缓存，故单独把缓存单张码表的方法提出来，跨类调用，使得缓存生效。
@Service
public class GgCodeTabExtService extends BaseService<GgCodeTab> implements IGgCodeTabExtService {

    @Override
    @Cacheable(value = Cache.DOC, key = "#codeType")
    public List<GgCodeTab> queryListByCodeTab(String codeType) {
        GgCodeTab codeTab = new GgCodeTab();
        codeTab.setCodeType(codeType);
        return queryListByBean(codeTab);
    }
}
