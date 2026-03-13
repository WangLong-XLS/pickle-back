package com.pickle.sys.service;


import com.pickle.sys.bean.SsJbxx;
import com.pickle.utils.base.IBaseService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ISsJbxxService extends IBaseService<SsJbxx> {
    List<SsJbxx> queryPageList(SsJbxx ssJbxx);

    List<SsJbxx> exportByRange(int offset, int limit);

    void importBigExcel(MultipartFile file);
}