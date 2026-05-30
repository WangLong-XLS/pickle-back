package com.pickle.procedure.config;

import com.pickle.procedure.jwt.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    
    private final JwtTokenUtils jwtTokenUtils;
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtTokenUtils)
                .addPathPatterns("/**")  // 拦截所有请求
                .excludePathPatterns(
                        "/sysUser/login",      // 排除登录接口
                        "/sysUser/register",   // 排除注册接口
                        "/wxUser/login",   // 排除小程序登录接口
                        "/swagger-ui/**",   // 排除 Swagger
                        "/v3/api-docs/**",  // 排除 API 文档
                        "/doc.html"         // 排除 Knife4j
                );
    }
}