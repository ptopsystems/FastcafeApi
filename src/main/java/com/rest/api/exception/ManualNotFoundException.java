package com.rest.api.exception;

public class ManualNotFoundException extends RuntimeException {
    public ManualNotFoundException() {
    }

    public ManualNotFoundException(String message) {
        super(message);
    }

    public ManualNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
