package com.pickle.sys.service;

import com.pickle.sys.bean.DmSjXzqh;
import com.pickle.utils.base.IBaseService;

import java.util.List;

public interface IDmSjXzqhService extends IBaseService<DmSjXzqh> {
    List<DmSjXzqh> selectXzqhList(DmSjXzqh dmSjXzqh);

}