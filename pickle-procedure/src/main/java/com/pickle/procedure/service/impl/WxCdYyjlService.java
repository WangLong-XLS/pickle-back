package com.pickle.procedure.service.impl;

import com.pickle.procedure.bean.WxCdCcxx;
import com.pickle.procedure.bean.WxCdYyjl;
import com.pickle.procedure.mapper.WxCdCcxxMapper;
import com.pickle.procedure.mapper.WxCdYyjlMapper;
import com.pickle.procedure.service.IWxCdYyjlService;
import com.pickle.utils.base.BaseService;
import com.pickle.utils.exception.BizException;
import com.pickle.utils.uuid.UUIDUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WxCdYyjlService extends BaseService<WxCdYyjl> implements IWxCdYyjlService {
    private final WxCdYyjlMapper wxCdYyjlMapper;
    private final WxCdCcxxMapper cdCcxxMapper;

    @Override
    public List<WxCdYyjl> queryPageList(WxCdYyjl wxCdYyjl) {
        List<WxCdYyjl> list = wxCdYyjlMapper.queryPageList(wxCdYyjl);
        return list;
    }

    @Override
    public void saveData(WxCdYyjl wxCdYyjl) {
        WxCdCcxx wxCdCcxx = new WxCdCcxx();
        wxCdCcxx.setCcyyUuid(wxCdYyjl.getCcyyUuid());
        wxCdCcxx.setDqRq(wxCdYyjl.getYyRq());
        List<WxCdCcxx> ccxxList = cdCcxxMapper.selectCcList(wxCdCcxx);
        WxCdCcxx cdCcxx = ccxxList.getFirst();
        if (cdCcxx.getCcYw().subtract(wxCdYyjl.getYyRs()).compareTo(BigDecimal.ZERO) < 0){
            throw new BizException("当前预约人数已超出！目前余位" +cdCcxx.getCcYw());
        }

        wxCdYyjl.setYyjlUuid(UUIDUtil.newUUID());
        wxCdYyjlMapper.insertSelective(wxCdYyjl);
    }

    @Override
    public void updateData(WxCdYyjl wxCdYyjl) {
        WxCdCcxx wxCdCcxx = new WxCdCcxx();
        wxCdCcxx.setCcyyUuid(wxCdYyjl.getCcyyUuid());
        wxCdCcxx.setDqRq(wxCdYyjl.getYyRq());
        List<WxCdCcxx> ccxxList = cdCcxxMapper.selectCcList(wxCdCcxx);
        WxCdCcxx cdCcxx = ccxxList.getFirst();
        if (cdCcxx.getCcYw().subtract(wxCdYyjl.getYyRs()).compareTo(BigDecimal.ZERO) < 0){
            throw new BizException("当前预约人数已超出！目前余位" +cdCcxx.getCcYw());
        }

        wxCdYyjlMapper.updateByPrimaryKeySelective(wxCdYyjl);
    }
}
