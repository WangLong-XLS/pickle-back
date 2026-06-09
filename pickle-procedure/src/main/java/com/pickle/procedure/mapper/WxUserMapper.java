package com.pickle.procedure.mapper;

import com.pickle.procedure.bean.WxUser;
import com.pickle.utils.base.BaseMapper;

import java.util.List;

public interface WxUserMapper extends BaseMapper<WxUser> {
    List<WxUser> queryPageList(WxUser wxUser);
}