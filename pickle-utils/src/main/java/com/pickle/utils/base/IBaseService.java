package com.pickle.utils.base;

import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

public interface IBaseService<T extends BaseBean> {
    int deleteByPrimaryKey(String pk);

    int save(T data);

    T queryByPrimaryKey(String pk);

    T queryFirstByBean(T t);

    T queryFirstByMap(Map<String, Object> map);

    long queryCountByBean(T t);

    long queryCountByMap(Map<String, Object> map);

    long queryCountAll();

    List<T> queryListByBean(T t);

    List<T> queryListByMap(Map<String, Object> map);

    List<T> queryListAll();

    int update(T data);

    int updateAllColumns(T data);

    int deleteByMap(Map<String, Object> map);

    int deleteByBean(T t);

    void batchSave(List<T> ts);

    void batchUpdate(List<T> ts);

    void batchDeleteByPrimaryKey(List<String> ts);

    PageInfo<T> getPage(List<T> ts, T t);
}
