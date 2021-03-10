package com.rest.api.exception;

public class NoticeNotFoundException extends RuntimeException {
    public NoticeNotFoundException() {
    }

    public NoticeNotFoundException(String message) {
        super(message);
    }

    public NoticeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
