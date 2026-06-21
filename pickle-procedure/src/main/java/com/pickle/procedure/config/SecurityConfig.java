package com.pickle.procedure.config;

import com.pickle.procedure.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /** 不需要 JWT 校验的白名单路径 */
    private static final String[] WHITE_LIST = {
            "/sysUser/login",
            "/sysUser/register",
            "/wxUser/login",
            "/pickle/swagger-ui/**",
            "/pickle/swagger-ui.html",
            "/pickle/v3/api-docs/**",
            "/pickle/doc.html"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. 关闭 CSRF（前后端分离，无 Session）
                .csrf(AbstractHttpConfigurer::disable)

                // 2. CORS：使用 CorsConfig 提供的 CorsConfigurationSource Bean
                //    这样 Spring Security 会在过滤器链最前面处理 CORS（包括 preflight OPTIONS），
                //    确保 OPTIONS 请求先被放行，再进入 JWT 校验
                .cors(Customizer.withDefaults())

                // 3. 配置 Session 为无状态
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 4. 白名单放行，其余需要认证
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(WHITE_LIST).permitAll()
                        .anyRequest().authenticated()
                )

                // 5. 将 JWT 过滤器插入到 UsernamePasswordAuthenticationFilter 之前
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
