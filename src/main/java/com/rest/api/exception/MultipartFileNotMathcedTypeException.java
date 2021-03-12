package com.rest.api.exception;

public class MultipartFileNotMathcedTypeException extends RuntimeException {
    public MultipartFileNotMathcedTypeException() {
    }

    public MultipartFileNotMathcedTypeException(String message) {
        super(message);
    }

    public MultipartFileNotMathcedTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}
