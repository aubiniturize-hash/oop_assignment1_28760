package com.auca.exceptions;

// Thrown when an ID, code, email or receipt number already exists.
public class DuplicateRecordException extends Exception {

    public DuplicateRecordException(String message) {
        super(message);
    }
}
