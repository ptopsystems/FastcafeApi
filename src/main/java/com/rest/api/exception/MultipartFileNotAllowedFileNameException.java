package com.rest.api.exception;

public class MultipartFileNotAllowedFileNameException extends RuntimeException {
    public MultipartFileNotAllowedFileNameException() {
    }

    public MultipartFileNotAllowedFileNameException(String message) {
        super(message);
    }

    public MultipartFileNotAllowedFileNameException(String message, Throwable cause) {
        super(message, cause);
    }
}
