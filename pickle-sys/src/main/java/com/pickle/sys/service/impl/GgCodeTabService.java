package com.pickle.sys.service.impl;

import com.pickle.sys.bean.GgCodeTab;
import com.pickle.sys.mapper.GgCodeTabMapper;
import com.pickle.sys.service.IGgCodeTabExtService;
import com.pickle.sys.service.IGgCodeTabService;
import com.pickle.utils.base.BaseService;
import com.pickle.utils.constant.Cache;
import com.pickle.utils.exception.BizException;
import com.pickle.utils.uuid.UUIDUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GgCodeTabService extends BaseService<GgCodeTab> implements IGgCodeTabService {
    private final GgCodeTabMapper ggCodeTabMapper;
    private final IGgCodeTabExtService ggCodeTabExtService;

    @Override
    @Cacheable(value = Cache.DOC, key = "#ggCodeTab.codeType")
    public List<GgCodeTab> queryCodeInfoList(GgCodeTab ggCodeTab) {
        return ggCodeTabMapper.queryCodeInfoList(ggCodeTab);
    }

    @Override
    public Map<String, Map<String, String>> queryCodeMapList(GgCodeTab ggCodeTab) {
        Map<String, Map<String, String>> resMap = new HashMap<>();
        ggCodeTab.getCodeTypeIn().forEach(e ->{
            List<GgCodeTab> list = ggCodeTabExtService.queryListByCodeTab(e);
            Map<String, String> collect = list.stream().collect(Collectors.toMap(GgCodeTab::getCode, GgCodeTab::getName));
            resMap.put(e, collect);
        });
        return resMap;
    }

    @Override
    @CacheEvict(value = Cache.DOC, key = "#ggCodeTab.codeType")
    public void saveData(GgCodeTab ggCodeTab) {
        GgCodeTab codeTab = new GgCodeTab();
        codeTab.setCodeType(ggCodeTab.getCodeType());
        codeTab.setCode(ggCodeTab.getCode());
        if (ggCodeTabMapper.selectCountByBean(codeTab) > 0){
            throw new BizException("编码不能重复");
        }
        codeTab = new GgCodeTab();
        codeTab.setCodeType(ggCodeTab.getCodeType());
        codeTab.setName(ggCodeTab.getName());
        if (ggCodeTabMapper.selectCountByBean(codeTab) > 0){
            throw new BizException("名称不能重复");
        }

        ggCodeTab.setCodeTabUuid(UUIDUtil.newUUID());
        ggCodeTabMapper.insertSelective(ggCodeTab);
    }

    @Override
    @CacheEvict(value = Cache.DOC, key = "#ggCodeTab.codeType")
    public void updateData(GgCodeTab ggCodeTab) {
        GgCodeTab codeTab = new GgCodeTab();
        codeTab.setCodeType(ggCodeTab.getCodeType());
        codeTab.setCode(ggCodeTab.getCode());
        List<GgCodeTab> codeList = ggCodeTabMapper.selectListByBean(codeTab);
        codeList.forEach(e ->{
            if (!e.getCodeTabUuid().equals(ggCodeTab.getCodeTabUuid())){
                throw new BizException("编码不能重复");
            }
        });
        codeTab = new GgCodeTab();
        codeTab.setCodeType(ggCodeTab.getCodeType());
        codeTab.setName(ggCodeTab.getName());
        List<GgCodeTab> nameList = ggCodeTabMapper.selectListByBean(codeTab);
        nameList.forEach(e ->{
            if (!e.getCodeTabUuid().equals(ggCodeTab.getCodeTabUuid())){
                throw new BizException("名称不能重复");
            }
        });

        ggCodeTabMapper.updateByPrimaryKeySelective(ggCodeTab);
    }

    @Override
    public Map<String, Map<String, String>> queryCodeMapForCode2Name(List<String> codeTypeList) {
        Map<String, Map<String, String>> resMap = new HashMap<>();
        codeTypeList.forEach(e -> resMap.put(e, this.queryCodeMapForCode2Name(e)));
        return resMap;
    }

    @Override
    public Map<String, String> queryCodeMapForCode2Name(String codeType) {
        List<GgCodeTab> list = ggCodeTabExtService.queryListByCodeTab(codeType);
        return list.stream().collect(Collectors.toMap(GgCodeTab::getCode, GgCodeTab::getName));
    }

    @Override
    public Map<String, Map<String, String>> queryCodeMapForName2Code(List<String> codeTypeList) {
        Map<String, Map<String, String>> resMap = new HashMap<>();
        codeTypeList.forEach(e -> resMap.put(e, this.queryCodeMapForName2Code(e)));
        return resMap;
    }

    @Override
    public Map<String, String> queryCodeMapForName2Code(String codeType) {
        List<GgCodeTab> list = ggCodeTabExtService.queryListByCodeTab(codeType);
        return list.stream().collect(Collectors.toMap(GgCodeTab::getName, GgCodeTab::getCode));
    }
}