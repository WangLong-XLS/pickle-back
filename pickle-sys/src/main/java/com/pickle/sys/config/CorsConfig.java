package com.pickle.sys.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();

        // ✅ 修改：允许前端地址（开发环境）
        configuration.addAllowedOrigin("http://localhost:8080");
        // 或者允许所有来源（仅开发环境）
        // configuration.addAllowedOriginPattern("*");

        configuration.addAllowedMethod("*"); // 允许所有请求方法
        configuration.addAllowedHeader("*"); // 允许所有请求头
        configuration.setAllowCredentials(true); // 允许携带凭证

        // 关键：添加暴露的头部
        configuration.setExposedHeaders(Arrays.asList(
                "Content-Disposition",
                "content-disposition",
                "Authorization",
                "Cache-Control",
                "Token"  // 添加 token 响应头
        ));

        source.registerCorsConfiguration("/**", configuration);
        return new CorsFilter(source);
    }
}