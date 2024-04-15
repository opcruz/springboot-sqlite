package com.demo.sqlite.exception;

public class ValidationError extends RuntimeException {

    public ValidationError() {
    }

    public ValidationError(String message) {
        super(message);
    }
}
