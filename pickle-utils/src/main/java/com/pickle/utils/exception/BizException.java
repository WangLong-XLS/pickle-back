package com.pickle.utils.exception;

import lombok.Data;

@Data
public class BizException extends RuntimeException {
    private String message;

    public BizException(String message) {
        this.message = message;
    }

    public BizException() {
    }

}