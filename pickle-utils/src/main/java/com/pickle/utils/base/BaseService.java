package com.pickle.utils.base;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class BaseService<T extends BaseBean> implements IBaseService<T>{

    @Autowired
    protected BaseMapper<T> mapper;

    public int deleteByPrimaryKey(String pk) {
        return mapper.deleteByPrimaryKey(pk);
    }

    public int save(T data) {
        return mapper.insertSelective(data);
    }

    public T queryByPrimaryKey(String pk) {
        return mapper.selectByPrimaryKey(pk);
    }

    public T queryFirstByBean(T data) {
        PageHelper.startPage(1, 1, false);
        List<T> ts = this.queryListByBean(data);
        return ts != null && !ts.isEmpty() ? ts.getFirst() : null;
    }

    public T queryFirstByMap(Map<String, Object> map) {
        PageHelper.startPage(1, 1, false);
        List<T> ts = mapper.selectListByMap(map);
        return ts != null && !ts.isEmpty() ? ts.getFirst() : null;
    }

    public List<T> queryListByBean(T data) {
        return mapper.selectListByBean(data);
    }

    public List<T> queryListByMap(Map<String, Object> map) {
        return mapper.selectListByMap(map);
    }

    public List<T> queryListAll() {
        return mapper.selectListByMap(Collections.emptyMap());
    }

    public long queryCountByBean(T data) {
        return mapper.selectCountByBean(data);
    }

    public long queryCountByMap(Map<String, Object> map) {
        return mapper.selectCountByMap(map);
    }

    public long queryCountAll() {
        return mapper.selectCountByMap(Collections.emptyMap());
    }

    public int update(T data) {
        return mapper.updateByPrimaryKeySelective(data);
    }

    public int updateAllColumns(T data) {
        return mapper.updateByPrimaryKey(data);
    }

    public int deleteByMap(Map<String, Object> map) {
        return mapper.deleteByMap(map);
    }

    public int deleteByBean(T data) {
        return mapper.deleteByBean(data);
    }

    public void batchSave(List<T> ts) {
        mapper.batchInsertSelective(ts);
    }

    public void batchUpdate(List<T> ts) {
        mapper.batchUpdate(ts);
    }

    public void batchDeleteByPrimaryKey(List<String> pks) {
        mapper.batchDeleteByPrimaryKey(pks);
    }

    public PageInfo<T> getPage(List<T> ts, T t) {
        return new PageInfo<>(ts);
    }
}
