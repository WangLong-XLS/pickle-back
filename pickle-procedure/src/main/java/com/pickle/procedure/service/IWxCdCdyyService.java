package com.pickle.procedure.service;

import com.pickle.procedure.bean.WxCdCdyy;
import com.pickle.utils.base.IBaseService;

import java.util.List;
import java.util.Map;

public interface IWxCdCdyyService extends IBaseService<WxCdCdyy> {
    Map<String, List<WxCdCdyy>> selectMapByBean(WxCdCdyy wxCdCdyy);
}