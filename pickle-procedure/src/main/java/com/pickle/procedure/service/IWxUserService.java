package com.pickle.procedure.service;

import com.pickle.procedure.bean.WxUser;
import com.pickle.utils.base.IBaseService;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IWxUserService extends IBaseService<WxUser> {
    WxUser login(WxUser sysUser);

    WxUser uploadAvatar(MultipartFile file, WxUser wxUser);

    List<WxUser> queryPageList(WxUser wxUser);

    void updateData(@Valid WxUser wxUser);
}