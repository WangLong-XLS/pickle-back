package com.pickle.utils.enums;

import com.power.common.interfaces.IMessage;

/**
 * 性别枚举
 */
public enum ManOrWom implements IMessage {
    MAN("M", "男"),
    WOMAN("W", "女");

    final String code;
    final String msg;

    ManOrWom(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.msg;
    }
}