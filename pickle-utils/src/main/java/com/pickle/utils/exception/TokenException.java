package com.pickle.utils.exception;

import lombok.Data;

@Data
public class TokenException extends RuntimeException {
    private String message;

    public TokenException(String message) {
        this.message = message;
    }

    public TokenException() {
    }

}