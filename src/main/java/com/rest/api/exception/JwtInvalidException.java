package com.rest.api.exception;

public class JwtInvalidException extends RuntimeException {
    public JwtInvalidException() {
        super();
    }

    public JwtInvalidException(String message) {
        super(message);
    }

    public JwtInvalidException(String message, Throwable cause) {
        super(message, cause);
    }
}
