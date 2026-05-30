package com.pickle.procedure.mapper;

import com.pickle.procedure.bean.WxCdCdxx;
import com.pickle.utils.base.BaseMapper;

import java.util.List;

public interface WxCdCdxxMapper extends BaseMapper<WxCdCdxx> {
    List<WxCdCdxx> selectListCheckout(WxCdCdxx wxCdCdxx);

}