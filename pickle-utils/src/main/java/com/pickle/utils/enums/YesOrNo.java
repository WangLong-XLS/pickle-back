package com.pickle.utils.enums;

import com.power.common.interfaces.IMessage;

/**
 * 是否枚举
 */
public enum YesOrNo implements IMessage {
    YES("Y","是"),
    NO("N","否");

    final String code;
    final String msg;

    YesOrNo(String code, String msg) {
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
