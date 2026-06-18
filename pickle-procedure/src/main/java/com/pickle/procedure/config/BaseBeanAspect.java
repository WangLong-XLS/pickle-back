package com.pickle.procedure.config;

import com.github.pagehelper.util.StringUtil;
import com.pickle.procedure.bean.WxUser;
import com.pickle.sys.bean.SysUser;
import com.pickle.utils.base.BaseBean;
import com.pickle.utils.exception.TokenException;
import com.pickle.utils.redis.RedisCacheService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Date;

@Aspect
@Component
public class BaseBeanAspect {
    private static final Logger logger = LoggerFactory.getLogger(BaseBeanAspect.class);
    @Autowired
    private RedisCacheService redisCacheService;

    @Autowired
    private HttpServletRequest request;

    @Before("execution(* com.pickle.*.controller.*.*(..)) &&" +
            "!execution(* com.pickle.sys.controller.SysUserController.login(..)) &&" +
            "!execution(* com.pickle.procedure.controller.WxUserController.login(..))"
    )
    public void setCommonFields(JoinPoint joinPoint) {
        logger.info("进入aop切面，给公共字段赋值");
        Object cache = this.getUserInfo();

        String userId = "test_user_code";
        String orgCode = "test_org_code";

        if (cache instanceof SysUser sysUser) {
            userId =  StringUtil.isNotEmpty(sysUser.getUserUuid()) ? sysUser.getUserUuid() : "test_user_code";
            orgCode = StringUtil.isNotEmpty(sysUser.getOrgCode()) ? sysUser.getOrgCode() : "test_org_code";

        } else if (cache instanceof WxUser wxUser) {
            userId =  StringUtil.isNotEmpty(wxUser.getUserUuid()) ? wxUser.getUserUuid() : "test_user_code";
        }

        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof BaseBean baseBean) {
                Date date = new Date();

                baseBean.setCjRyDm(StringUtil.isNotEmpty(baseBean.getCjRyDm()) ? baseBean.getCjRyDm() : userId);
                baseBean.setCjSj(baseBean.getCjSj() != null ? baseBean.getCjSj() : date);
                baseBean.setXgRyDm(userId);
                baseBean.setXgSj(date);
                baseBean.setSjgsJgDm(StringUtil.isNotEmpty(baseBean.getSjgsJgDm()) ? baseBean.getSjgsJgDm() : orgCode);
            }
        }
    }

    public Object getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new TokenException("未登录或登录已过期");
        }

        Object principal = authentication.getPrincipal();
        // principal 就是 JwtAuthenticationFilter 里放进去的 SysUser 或 WxUser
        if (principal instanceof SysUser || principal instanceof WxUser) {
            return principal;
        }

        throw new TokenException("用户信息类型异常");
    }

}
