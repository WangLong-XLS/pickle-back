package com.pickle.procedure.mapper;

import com.pickle.procedure.bean.WxCdYyjl;
import com.pickle.utils.base.BaseMapper;

import java.util.List;

public interface WxCdYyjlMapper extends BaseMapper<WxCdYyjl> {
    List<WxCdYyjl> queryPageList(WxCdYyjl wxCdYyjl);
}