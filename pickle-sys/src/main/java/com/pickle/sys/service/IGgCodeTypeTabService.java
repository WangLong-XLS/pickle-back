package com.pickle.sys.service;

import com.pickle.sys.bean.GgCodeTypeTab;
import com.pickle.utils.base.IBaseService;

import java.util.List;

public interface IGgCodeTypeTabService extends IBaseService<GgCodeTypeTab> {
    List<GgCodeTypeTab> queryPageList(GgCodeTypeTab ggCodeTypeTab);

    void saveData(GgCodeTypeTab ggCodeTypeTab);

    void updateData(GgCodeTypeTab ggCodeTypeTab);

    void deleteData(GgCodeTypeTab ggCodeTypeTab);
}