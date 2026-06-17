package com.pickle.procedure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Value("${vue.url:http://localhost:8080}")
    private String url;

    /**
     * 提供 CorsConfigurationSource Bean，
     * 被 SecurityConfig 的 .cors(withDefaults()) 自动引用。
     * 也可以被独立 CorsFilter 使用（如果需要）。
     */
    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOrigin(url);
        configuration.addAllowedMethod("*");   // 允许所有请求方法
        configuration.addAllowedHeader("*");   // 允许所有请求头
        configuration.setAllowCredentials(true);

        // 暴露的头部
        configuration.setExposedHeaders(Arrays.asList(
                "Content-Disposition",
                "content-disposition",
                "Authorization",
                "Cache-Control",
                "Token"
        ));

        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
