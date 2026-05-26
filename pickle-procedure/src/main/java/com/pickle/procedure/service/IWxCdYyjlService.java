package com.pickle.procedure.service;

import com.pickle.procedure.bean.WxCdYyjl;
import com.pickle.utils.base.IBaseService;

import java.util.List;

public interface IWxCdYyjlService extends IBaseService<WxCdYyjl> {
    List<WxCdYyjl> queryPageList(WxCdYyjl wxCdYyjl);
}