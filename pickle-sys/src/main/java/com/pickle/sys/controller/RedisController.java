package com.pickle.sys.controller;

import com.pickle.sys.bean.RedisBean;
import com.pickle.sys.service.IRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/redis")
@RequiredArgsConstructor
public class RedisController {
    private final IRedisService redisService;

    @RequestMapping("/clearCache")
    public void clearCache(@RequestBody RedisBean redisBean) {
        redisService.deleteCacheByPrefix(redisBean.getClearCacheType());
    }
}
