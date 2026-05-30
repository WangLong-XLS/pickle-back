package com.pickle.procedure.service.impl;

import com.alibaba.excel.EasyExcel;
import com.pickle.procedure.bean.WxCdCdxx;
import com.pickle.procedure.bean.entity.WxCdCdxxEntity;
import com.pickle.procedure.mapper.WxCdCdxxMapper;
import com.pickle.procedure.service.IWxCdCdxxService;
import com.pickle.utils.base.BaseService;
import com.pickle.utils.bean.BeanUtils;
import com.pickle.utils.exception.BizException;
import com.pickle.utils.uuid.UUIDUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class WxCdCdxxService extends BaseService<WxCdCdxx> implements IWxCdCdxxService {
    private final WxCdCdxxMapper wxCdCdxxMapper;

    @Override
    public List<WxCdCdxx> selectCdList(WxCdCdxx wxCdCdxx) {
        List<WxCdCdxx> list = wxCdCdxxMapper.selectListByBean(wxCdCdxx);
        return list;
    }

    @Override
    public List<WxCdCdxx> queryPageList(WxCdCdxx wxCdCdxx) {
        List<WxCdCdxx> list = wxCdCdxxMapper.selectListByBean(wxCdCdxx);
        return list;
    }

    @Override
    public void saveData(WxCdCdxx wxCdCdxx) {
        WxCdCdxx cdxx = new WxCdCdxx();
        cdxx.setCdMc(wxCdCdxx.getCdMc());
        List<WxCdCdxx> list = wxCdCdxxMapper.selectListByBean(cdxx);
        if (!list.isEmpty()) {
            throw new BizException("场地名称（" +wxCdCdxx.getCdMc() +"）不能重复！");
        }

        wxCdCdxx.setCdxxUuid(UUIDUtil.newUUID());
        wxCdCdxxMapper.insertSelective(wxCdCdxx);
    }

    @Override
    public void updateData(WxCdCdxx wxCdCdxx) {
        WxCdCdxx cdxx = new WxCdCdxx();
        cdxx.setCdMc(wxCdCdxx.getCdMc());
        List<WxCdCdxx> list = wxCdCdxxMapper.selectListByBean(cdxx);
        list.forEach(e ->{
            if (!e.getCdxxUuid().equals(wxCdCdxx.getCdxxUuid())) {
                throw new BizException("场地名称（" +e.getCdMc() +"）不能重复！");
            }
        });
        wxCdCdxxMapper.updateByPrimaryKeySelective(wxCdCdxx);
    }

    @Override
    public void importExcel(MultipartFile file) {
        try {
            List<WxCdCdxxEntity> dataList = EasyExcel.read(file.getInputStream())
                    .sheet()
                    .head(WxCdCdxxEntity.class)
                    .headRowNumber(1)
                    .doReadSync();

            List<WxCdCdxx> list = BeanUtils.copyPropertiesList(dataList, WxCdCdxx.class);
            Set<String> set = new HashSet<>();

            list.forEach(e -> {
                set.add(e.getCdMc());
                e.setCdxxUuid(UUIDUtil.newUUID());
            });

            if (set.size() != list.size()) {
                throw new BizException("导入文件中有重复场地名称！");
            }

            WxCdCdxx cdxx = new WxCdCdxx();
            cdxx.setCdMcIn(new ArrayList<>(set));
            List<String> checkoutList = wxCdCdxxMapper.selectListCheckout(cdxx)
                    .stream()
                    .map(WxCdCdxx::getCdMc)
                    .toList();
            if (!checkoutList.isEmpty()) {
                throw new BizException("场地名称（" +checkoutList +"）不能重复！");
            }
            wxCdCdxxMapper.batchInsert(list);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}