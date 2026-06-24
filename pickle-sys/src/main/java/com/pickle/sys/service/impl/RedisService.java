package com.pickle.sys.service.impl;

import com.pickle.sys.service.IRedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisService implements IRedisService {
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    // 存入缓存（带过期时间）
    @Override
    public void setCache(String key, Object value, long timeout, TimeUnit unit) {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        operations.set(key, value, timeout, unit);
    }
    
    // 获取缓存
    @Override
    public Object getCache(String key) {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        return operations.get(key);
    }

    @Override
    // 删除缓存
    public void deleteCache(String key) {
        redisTemplate.delete(key);
    }
    
    // 检查缓存是否存在
    @Override
    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public void deleteCacheByPrefix(String prefix) {
        String pattern = prefix + "*";
        Set<String> keys = redisTemplate.keys(pattern);
        if (!keys.isEmpty()) {
            redisTemplate.delete(keys);
            log.info("已删除 {} 个以 '{}' 开头的缓存", keys.size(), prefix);
        } else {
            log.info("没有找到以 '{}' 开头的缓存", prefix);
        }
    }
}