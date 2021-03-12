package com.rest.api.exception;

public class MultipartFileNotAllowedTypeException extends RuntimeException {
    public MultipartFileNotAllowedTypeException() {
    }

    public MultipartFileNotAllowedTypeException(String message) {
        super(message);
    }

    public MultipartFileNotAllowedTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}
