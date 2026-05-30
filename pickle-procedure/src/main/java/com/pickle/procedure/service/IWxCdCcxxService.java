package com.pickle.procedure.service;

import com.pickle.procedure.bean.WxCdCcxx;
import com.pickle.utils.base.IBaseService;
import jakarta.validation.Valid;

import java.util.List;

public interface IWxCdCcxxService extends IBaseService<WxCdCcxx> {
    List<WxCdCcxx> selectCcList(WxCdCcxx wxCdCcxx);

    List<WxCdCcxx> queryPageList(WxCdCcxx wxCdCcxx);

    void saveData(@Valid WxCdCcxx wxCdCcxx);

    void updateData(@Valid WxCdCcxx wxCdCcxx);
}