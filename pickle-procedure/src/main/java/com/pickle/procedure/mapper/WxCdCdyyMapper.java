package com.pickle.procedure.mapper;

import com.pickle.procedure.bean.WxCdCdyy;
import com.pickle.utils.base.BaseMapper;

import java.util.List;

public interface WxCdCdyyMapper extends BaseMapper<WxCdCdyy> {
    List<WxCdCdyy> selectMapByBean(WxCdCdyy wxCdCdyy);

}