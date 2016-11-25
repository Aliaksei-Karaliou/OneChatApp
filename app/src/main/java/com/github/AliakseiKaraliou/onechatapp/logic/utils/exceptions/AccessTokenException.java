package com.github.aliakseiKaraliou.onechatapp.logic.utils.exceptions;

public class AccessTokenException extends Exception {
    public AccessTokenException() {
        super();
    }

    public AccessTokenException(String message) {
        super(message);
    }

    public AccessTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccessTokenException(Throwable cause) {
        super(cause);
    }
}
