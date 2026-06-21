package com.pickle.sys.mapper;

import com.pickle.sys.bean.GgCodeTab;
import com.pickle.utils.base.BaseMapper;

import java.util.List;

public interface GgCodeTabMapper extends BaseMapper<GgCodeTab> {
    List<GgCodeTab> queryCodeInfoList(GgCodeTab ggCodeTab);
}