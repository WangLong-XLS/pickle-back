package com.pickle.procedure.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.util.StringUtil;
import com.pickle.procedure.bean.WxUser;
import com.pickle.sys.bean.SysUser;
import com.pickle.utils.redis.RedisCacheService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * JWT 认证过滤器（Spring Security Filter 方式）
 * 在 UsernamePasswordAuthenticationFilter 之前执行
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final RedisCacheService redisCacheService;

    /** 不需要 JWT 校验的白名单路径 */
    private static final String[] WHITE_LIST = {
            "/sysUser/login",
            "/sysUser/register",
            "/wxUser/login",
            "/pickle/swagger-ui/**",
            "/pickle/v3/api-docs/**",
            "/pickle/doc.html"
    };

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    /**
     * 白名单路径跳过 JWT 校验
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        for (String pattern : WHITE_LIST) {
            if (pathMatcher.match(pattern, path)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        log.debug("开始处理token，请求路径：{}", request.getServletPath());

        String token = request.getHeader("Token");
        token = StringUtil.isNotEmpty(token) ? token : request.getParameter("Token");

        if (StringUtil.isEmpty(token)) {
            writeError(response, "无token，请重新登录");
            return;
        }

        String userId;
        Object cache;

        try {
            userId = JWT.decode(token).getAudience().getFirst();
            cache = redisCacheService.getCache(userId);

            if (cache == null) {
                writeError(response, "该用户信息不存在，请重新登录");
                return;
            }

            String secret = resolveSecret(cache);
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret)).build();
            verifier.verify(token);

        } catch (JWTVerificationException e) {
            writeError(response, "token已过期，请重新登录");
            return;
        } catch (Exception e) {
            log.error("token解析异常", e);
            writeError(response, "token解析出现问题，请联系管理员");
            return;
        }

        // ========== 关键：token 验证通过后设置 SecurityContext ==========
        // 否则 Spring Security 的 FilterSecurityInterceptor 会认为未认证 → 403
        if (cache instanceof SysUser user) {
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else if (cache instanceof WxUser wxUser) {
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(wxUser, null, new ArrayList<>());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // ← 新增：刷新 Redis TTL，续期 30 分钟
        redisCacheService.setCache(userId, cache, 60 * 30, TimeUnit.SECONDS);
        log.info("token验证通过，以 {} 方式请求：{}", request.getMethod(), request.getRequestURI());
        try {
            filterChain.doFilter(request, response);
        } finally {
            // 请求处理完毕后清除 SecurityContext，避免线程池复用导致上下文污染
            SecurityContextHolder.clearContext();
        }
    }

    /**
     * 根据缓存对象类型解析签名密钥
     */
    private String resolveSecret(Object cache) {
        if (cache instanceof SysUser user) {
            return user.getUserPassword();
        } else if (cache instanceof WxUser wxUser) {
            return wxUser.getWxCode();
        }
        throw new IllegalArgumentException("未知的用户类型");
    }

    /**
     * 向客户端写入 JSON 格式的错误响应
     */
    private void writeError(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> body = new HashMap<>();
        body.put("code", 401);
        body.put("message", message);

        new ObjectMapper().writeValue(response.getWriter(), body);
    }
}
