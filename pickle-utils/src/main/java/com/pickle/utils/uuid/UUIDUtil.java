package com.pickle.utils.uuid;

import com.pickle.utils.constant.StringConstant;

import java.util.UUID;

public final class UUIDUtil {
    public static String newUUID() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replace(StringConstant.HYPHEN_LINE, StringConstant.EMPTY);
    }
}
