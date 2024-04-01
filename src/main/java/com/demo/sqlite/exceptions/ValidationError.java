package com.demo.sqlite.exceptions;

public class ValidationError extends Exception {

    public ValidationError() {
    }

    public ValidationError(String message) {
        super(message);
    }
}
