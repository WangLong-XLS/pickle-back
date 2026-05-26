package com.pickle.utils.enums;

/**
 * 是否枚举
 */
public enum YesOrNo {
    YES("Y", "是"),
    NO("N", "否");

    String code;
    String msg;

    YesOrNo(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    // 根据 code 获取枚举
    public static YesOrNo getByCode(String code) {
        for (YesOrNo value : values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }
}
