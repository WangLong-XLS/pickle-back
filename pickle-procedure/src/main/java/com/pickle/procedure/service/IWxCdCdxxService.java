package com.pickle.procedure.service;

import com.pickle.procedure.bean.WxCdCdxx;
import com.pickle.utils.base.IBaseService;

import java.util.List;

public interface IWxCdCdxxService extends IBaseService<WxCdCdxx> {
    List<WxCdCdxx> selectCdList(WxCdCdxx wxCdCdxx);
}