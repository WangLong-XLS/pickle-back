package com.pickle.sys.mapper;

import com.pickle.sys.bean.GgCodeTypeTab;
import com.pickle.utils.base.BaseMapper;

import java.util.List;

public interface GgCodeTypeTabMapper extends BaseMapper<GgCodeTypeTab> {
    List<GgCodeTypeTab> queryPageList(GgCodeTypeTab ggCodeTypeTab);
}