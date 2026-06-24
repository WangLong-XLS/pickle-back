package com.pickle.sys.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public interface IRedisService {

    // 存入缓存（带过期时间）
    void setCache(String key, Object value, long timeout, TimeUnit unit);

    // 获取缓存
    Object getCache(String key);

    // 删除缓存
    void deleteCache(String key);

    // 检查缓存是否存在
    boolean hasKey(String key);

    //删除以什么开头的缓存
    void deleteCacheByPrefix(String key);
}