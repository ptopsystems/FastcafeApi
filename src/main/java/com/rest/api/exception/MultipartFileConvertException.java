package com.rest.api.exception;

public class MultipartFileConvertException extends RuntimeException {
    public MultipartFileConvertException() {
    }

    public MultipartFileConvertException(String message) {
        super(message);
    }

    public MultipartFileConvertException(String message, Throwable cause) {
        super(message, cause);
    }
}
