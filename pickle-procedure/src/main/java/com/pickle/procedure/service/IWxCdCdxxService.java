package com.pickle.procedure.service;

import com.pickle.procedure.bean.WxCdCdxx;
import com.pickle.utils.base.IBaseService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IWxCdCdxxService extends IBaseService<WxCdCdxx> {
    List<WxCdCdxx> selectCdList(WxCdCdxx wxCdCdxx);

    List<WxCdCdxx> queryPageList(WxCdCdxx wxCdCdxx);

    void saveData(WxCdCdxx wxCdCdxx);

    void updateData(WxCdCdxx wxCdCdxx);

    void importExcel(MultipartFile file);
}