package com.pickle.utils.base;

import java.util.List;
import java.util.Map;

public interface BaseMapper<T extends BaseBean> {
    int deleteByPrimaryKey(String pk);

    int insert(T data);

    int insertSelective(T data);

    T selectByPrimaryKey(String pk);

    long selectCountByBean(T data);

    long selectCountByMap(Map<String, Object> map);

    List<T> selectListByBean(T data);

    List<T> selectListByMap(Map<String, Object> map);

    int updateByPrimaryKeySelective(T data);

    int updateByPrimaryKey(T data);

    void batchInsertSelective(List<T> ts);

    void batchInsert(List<T> ts);

    void batchUpdate(List<T> ts);

    void batchDeleteByPrimaryKey(List<String> ts);

    int deleteByMap(Map<String, Object> map);

    int deleteByBean(T data);
}
