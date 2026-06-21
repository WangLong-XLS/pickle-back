package com.pickle.sys.service.impl;

import com.pickle.sys.bean.GgCodeTab;
import com.pickle.sys.bean.GgCodeTypeTab;
import com.pickle.sys.mapper.GgCodeTabMapper;
import com.pickle.sys.mapper.GgCodeTypeTabMapper;
import com.pickle.sys.service.IGgCodeTypeTabService;
import com.pickle.utils.base.BaseService;
import com.pickle.utils.enums.YesOrNo;
import com.pickle.utils.exception.BizException;
import com.pickle.utils.uuid.UUIDUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GgCodeTypeTabService extends BaseService<GgCodeTypeTab> implements IGgCodeTypeTabService {
    private final GgCodeTypeTabMapper ggCodeTypeTabMapper;
    private final GgCodeTabMapper ggCodeTabMapper;

    @Override
    public List<GgCodeTypeTab> queryPageList(GgCodeTypeTab ggCodeTypeTab) {
        List<GgCodeTypeTab> list = ggCodeTypeTabMapper.queryPageList(ggCodeTypeTab);
        list.forEach(e -> e.setIsTreeMc(e.getIsTree().equals(YesOrNo.YES.getCode()) ? YesOrNo.YES.getMessage() : YesOrNo.NO.getMessage()));
        return list;
    }

    @Override
    public void saveData(GgCodeTypeTab ggCodeTypeTab) {
        GgCodeTypeTab codeTypeTab = new GgCodeTypeTab();
        codeTypeTab.setCode(ggCodeTypeTab.getCode());
        if (ggCodeTypeTabMapper.selectCountByBean(codeTypeTab) > 0){
            throw new BizException("编码不能重复");
        }
        codeTypeTab = new GgCodeTypeTab();
        codeTypeTab.setName(ggCodeTypeTab.getName());
        if (ggCodeTypeTabMapper.selectCountByBean(codeTypeTab) > 0){
            throw new BizException("名称不能重复");
        }

        ggCodeTypeTab.setCodeTypeTabUuid(UUIDUtil.newUUID());
        ggCodeTypeTabMapper.insertSelective(ggCodeTypeTab);
    }

    @Override
    public void updateData(GgCodeTypeTab ggCodeTypeTab) {
        GgCodeTypeTab codeTypeTab = new GgCodeTypeTab();
        codeTypeTab.setCode(ggCodeTypeTab.getCode());
        List<GgCodeTypeTab> codeList = ggCodeTypeTabMapper.selectListByBean(codeTypeTab);
        codeList.forEach(e ->{
            if (!e.getCodeTypeTabUuid().equals(ggCodeTypeTab.getCodeTypeTabUuid())){
                throw new BizException("编码不能重复");
            }
        });
        codeTypeTab = new GgCodeTypeTab();
        codeTypeTab.setName(ggCodeTypeTab.getName());
        List<GgCodeTypeTab> nameList = ggCodeTypeTabMapper.selectListByBean(codeTypeTab);
        nameList.forEach(e ->{
            if (!e.getCodeTypeTabUuid().equals(ggCodeTypeTab.getCodeTypeTabUuid())){
                throw new BizException("名称不能重复");
            }
        });

        ggCodeTypeTabMapper.updateByPrimaryKeySelective(ggCodeTypeTab);
    }

    @Override
    @Transactional
    public void deleteData(GgCodeTypeTab ggCodeTypeTab) {
        List<String> codeTypeTabUuidIn = ggCodeTypeTab.getCodeTypeTabUuidIn();
        if (!codeTypeTabUuidIn.isEmpty()){
            List<String> codeTypeIn = ggCodeTypeTabMapper.selectListByBean(ggCodeTypeTab).stream().map(GgCodeTypeTab::getCode).collect(Collectors.toList());

            GgCodeTab ggCodeTab = new GgCodeTab();
            ggCodeTab.setCodeTypeIn(codeTypeIn);
            ggCodeTabMapper.deleteByBean(ggCodeTab);
            ggCodeTypeTabMapper.batchDeleteByPrimaryKey(codeTypeTabUuidIn);
        }
    }
}