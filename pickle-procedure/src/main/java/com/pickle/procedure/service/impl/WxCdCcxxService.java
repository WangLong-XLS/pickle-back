package com.pickle.procedure.service.impl;

import com.pickle.procedure.bean.WxCdCcxx;
import com.pickle.procedure.mapper.WxCdCcxxMapper;
import com.pickle.procedure.service.IWxCdCcxxService;
import com.pickle.utils.base.BaseService;
import com.pickle.utils.enums.YesOrNo;
import com.pickle.utils.exception.BizException;
import com.pickle.utils.uuid.UUIDUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WxCdCcxxService extends BaseService<WxCdCcxx> implements IWxCdCcxxService {
    private final WxCdCcxxMapper wxCdCcxxMapper;

    @Override
    public List<WxCdCcxx> selectCcList(WxCdCcxx wxCdCcxx) {
        List<WxCdCcxx> list = wxCdCcxxMapper.selectCcList(wxCdCcxx);
        return list;
    }

    @Override
    public List<WxCdCcxx> queryPageList(WxCdCcxx wxCdCcxx) {
        List<WxCdCcxx> list = wxCdCcxxMapper.queryPageList(wxCdCcxx);
        list.forEach(e -> e.setHjtBzMc(e.getHjtBz().equals(YesOrNo.YES.getCode()) ? YesOrNo.YES.getMessage() : YesOrNo.NO.getMessage()));
        return list;
    }

    @Override
    public void saveData(WxCdCcxx wxCdCcxx) {
        WxCdCcxx ccxx = new WxCdCcxx();
        ccxx.setCdxxUuid(wxCdCcxx.getCdxxUuid());
        ccxx.setCcSd(wxCdCcxx.getCdxxUuid());
        List<WxCdCcxx> list = wxCdCcxxMapper.selectListByBean(wxCdCcxx);
        if (!list.isEmpty()) {
            throw new BizException("该时段已存在");
        }

        wxCdCcxx.setCcyyUuid(UUIDUtil.newUUID());
        wxCdCcxxMapper.insertSelective(wxCdCcxx);
    }

    @Override
    public void updateData(WxCdCcxx wxCdCcxx) {
        wxCdCcxxMapper.updateByPrimaryKeySelective(wxCdCcxx);
    }
}