package com.rest.api.exception;

public class JwtExpiredException extends RuntimeException {
    public JwtExpiredException() {
        super();
    }

    public JwtExpiredException(String message) {
        super(message);
    }

    public JwtExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
