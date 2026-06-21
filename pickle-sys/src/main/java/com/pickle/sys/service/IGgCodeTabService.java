package com.pickle.sys.service;

import com.pickle.sys.bean.GgCodeTab;
import com.pickle.utils.base.IBaseService;

import java.util.List;
import java.util.Map;

public interface IGgCodeTabService extends IBaseService<GgCodeTab> {
    List<GgCodeTab> queryCodeInfoList(GgCodeTab ggCodeTab);

    Map<String, Map<String, String>> queryCodeMapList(GgCodeTab ggCodeTab);

    void saveData(GgCodeTab ggCodeTab);

    void updateData(GgCodeTab ggCodeTab);

    //返回每个码表的map数据，代码转名称
    Map<String, Map<String,String>> queryCodeMapForCode2Name(List<String> codeTypeList);

    //返回单个码表的map数据，代码转名称
    Map<String,String> queryCodeMapForCode2Name(String codeType);

    //返回每个码表的map数据，名称转代码
    Map<String,Map<String,String>> queryCodeMapForName2Code(List<String> codeTypeList);

    //返回单个码表的map数据，名称转代码
    Map<String,String> queryCodeMapForName2Code(String codeType);

}