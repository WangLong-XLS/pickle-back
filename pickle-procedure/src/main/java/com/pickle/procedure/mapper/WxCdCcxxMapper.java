package com.pickle.procedure.mapper;

import com.pickle.procedure.bean.WxCdCcxx;
import com.pickle.utils.base.BaseMapper;

import java.util.List;

public interface WxCdCcxxMapper extends BaseMapper<WxCdCcxx> {
    List<WxCdCcxx> selectCcList(WxCdCcxx wxCdCcxx);
}