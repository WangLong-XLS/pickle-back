package com.pickle.sys.jwt;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.github.pagehelper.util.StringUtil;
import com.pickle.sys.bean.SysUser;
import com.pickle.utils.exception.TokenException;
import com.pickle.utils.redis.RedisCacheService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenUtils implements HandlerInterceptor {
    private final RedisCacheService redisCacheService;
    // 请求处理前
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.info("开始处理token");

        String token = request.getHeader("Token");
        token = StringUtil.isNotEmpty(token) ? token : request.getParameter("Token");

        if (StringUtil.isEmpty(token)){
            throw new TokenException("无token，请重新登录");
        }

        String userId = JWT.decode(token).getAudience().getFirst();
        SysUser user = (SysUser) redisCacheService.getCache(userId);
        if (user == null){
            throw new TokenException("该用户信息不存在，请重新登录");
        }

        JWTVerifier build = JWT.require(Algorithm.HMAC256(user.getUserPassword())).build();
        try {
            build.verify(token);
        } catch (JWTVerificationException e) {
            throw new TokenException("token已过期，请重新登录");
        }

        log.info("token验证通过，以" +request.getMethod() +"方式请求：" +request.getRequestURI());
        return true;  // 如果验证成功，继续处理请求
    }


    // 请求处理后，渲染视图前
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        log.info(request.getRequestURI() +"执行结束");
    }

    // 请求处理后，视图渲染后
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        log.info("请求处理后，视图渲染后");
    }
}
