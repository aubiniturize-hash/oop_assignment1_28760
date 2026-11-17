package com.auca.exceptions;

// Thrown when entered data breaks a validation rule (blank text, bad email, wrong range...).
public class ValidationException extends Exception {

    public ValidationException(String message) {
        super(message);
    }
}
