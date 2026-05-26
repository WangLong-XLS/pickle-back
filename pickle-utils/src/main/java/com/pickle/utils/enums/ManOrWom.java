package com.pickle.utils.enums;

/**
 * 性别枚举
 */
public enum ManOrWom {
    MAN("M", "男"),
    WOMAN("W", "女");

    private final String code;
    private final String msg;

    ManOrWom(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.msg;
    }

    // 根据 code 获取枚举
    public static ManOrWom getByCode(String code) {
        for (ManOrWom value : values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }

    // 根据 code 获取描述
    public static String getMessageByCode(String code) {
        ManOrWom e = getByCode(code);
        return e != null ? e.msg : null;
    }
}