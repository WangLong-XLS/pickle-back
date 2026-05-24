package com.pickle.procedure.service;

import com.pickle.procedure.bean.WxUser;
import com.pickle.utils.base.IBaseService;
import org.springframework.web.multipart.MultipartFile;

public interface IWxUserService extends IBaseService<WxUser> {
    WxUser login(WxUser sysUser);

    WxUser uploadAvatar(MultipartFile file, WxUser wxUser);
}