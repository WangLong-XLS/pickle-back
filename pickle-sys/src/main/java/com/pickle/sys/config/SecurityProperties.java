package com.pickle.sys.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 读取 YAML 中 spring.security 自定义配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "spring.security")
public class SecurityProperties {

    /** 单个用户最大 session 数量 */
    private int maxSessionNumber = 10;

    /** 是否启用必须登录访问 */
    private boolean enabled = true;

    /** 是否启用登录密码加密传输 */
    private boolean encryptedTransmissionPassword = true;

    /** 超级管理员配置 */
    private UserProperties user = new UserProperties();

    @Data
    public static class UserProperties {
        private String name;
        private String password;
    }
}
