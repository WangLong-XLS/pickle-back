package com.pickle.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan({
        "com.pickle.sys.mapper"
})
@ComponentScan(basePackages = {
        "com.pickle.utils",
        "com.pickle.web",
        "com.pickle.sys"
})
@EnableCaching  // 开启缓存注解支持
public class PickleBackApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(PickleBackApplication.class, args);
    }
}
