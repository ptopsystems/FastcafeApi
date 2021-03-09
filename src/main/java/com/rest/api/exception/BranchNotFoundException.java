package com.rest.api.exception;

public class BranchNotFoundException extends RuntimeException {
    public BranchNotFoundException() {
    }

    public BranchNotFoundException(String message) {
        super(message);
    }

    public BranchNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
