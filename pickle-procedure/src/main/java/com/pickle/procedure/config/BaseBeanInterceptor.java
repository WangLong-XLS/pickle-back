package com.pickle.procedure.config;

import com.pickle.procedure.bean.WxUser;
import com.pickle.sys.bean.SysUser;
import com.pickle.utils.base.BaseBean;
import com.pickle.utils.base.BaseBeanFillUtil;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Properties;

/**
 * MyBatis 拦截器 —— 在 SQL 执行层自动填充 BaseBean 公共字段。
 * 覆盖所有 INSERT / UPDATE 操作，无论调用路径是：
 * Controller → Service → Mapper 还是直接注入 Mapper 调用，全部生效。
 */
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
@Component
public class BaseBeanInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
        Object parameter = invocation.getArgs()[1];

        SqlCommandType sqlType = ms.getSqlCommandType();

        if (parameter == null || sqlType == null) {
            return invocation.proceed();
        }

        // 只处理 INSERT 和 UPDATE
        if (sqlType == SqlCommandType.INSERT) {
            fillInsertFields(parameter);
        } else if (sqlType == SqlCommandType.UPDATE) {
            fillUpdateFields(parameter);
        }

        return invocation.proceed();
    }

    // ==================== 填充逻辑 ====================

    private void fillInsertFields(Object parameter) {
        String userId = getCurrentUserId();
        String orgCode = getCurrentOrgCode();

        if (parameter instanceof BaseBean bean) {
            BaseBeanFillUtil.fillForInsert(bean, userId, orgCode);
        } else if (parameter instanceof Map<?, ?> map) {
            for (Object value : map.values()) {
                fillInsertIfBaseBean(value, userId, orgCode);
            }
        } else if (parameter instanceof Collection<?> collection) {
            for (Object item : collection) {
                fillInsertIfBaseBean(item, userId, orgCode);
            }
        }
    }

    private void fillUpdateFields(Object parameter) {
        String userId = getCurrentUserId();

        if (parameter instanceof BaseBean bean) {
            BaseBeanFillUtil.fillForUpdate(bean, userId);
        } else if (parameter instanceof Map<?, ?> map) {
            for (Object value : map.values()) {
                fillUpdateIfBaseBean(value, userId);
            }
        } else if (parameter instanceof Collection<?> collection) {
            for (Object item : collection) {
                fillUpdateIfBaseBean(item, userId);
            }
        }
    }

    private void fillInsertIfBaseBean(Object obj, String userId, String orgCode) {
        if (obj instanceof BaseBean bean) {
            BaseBeanFillUtil.fillForInsert(bean, userId, orgCode);
        } else if (obj instanceof Collection<?> nested) {
            for (Object item : nested) {
                fillInsertIfBaseBean(item, userId, orgCode);
            }
        }
    }

    private void fillUpdateIfBaseBean(Object obj, String userId) {
        if (obj instanceof BaseBean bean) {
            BaseBeanFillUtil.fillForUpdate(bean, userId);
        } else if (obj instanceof Collection<?> nested) {
            for (Object item : nested) {
                fillUpdateIfBaseBean(item, userId);
            }
        }
    }

    // ==================== 用户信息 ====================

    private String getCurrentUserId() {
        Object principal = getPrincipal();
        if (principal instanceof SysUser su) {
            String uuid = su.getUserUuid();
            return uuid != null && !uuid.isEmpty() ? uuid : "test_user_code";
        }
        if (principal instanceof WxUser wu) {
            String uuid = wu.getUserUuid();
            return uuid != null && !uuid.isEmpty() ? uuid : "test_user_code";
        }
        return "test_user_code";
    }

    private String getCurrentOrgCode() {
        Object principal = getPrincipal();
        if (principal instanceof SysUser su) {
            String orgCode = su.getOrgCode();
            return orgCode != null && !orgCode.isEmpty() ? orgCode : "test_org_code";
        }
        return "test_org_code";
    }

    private Object getPrincipal() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            return auth.getPrincipal();
        }
        return null;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }
}
