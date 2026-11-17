package com.auca.exceptions;

// Thrown when a student tries to borrow a book that has no copy left.
public class BookUnavailableException extends Exception {

    public BookUnavailableException(String message) {
        super(message);
    }
}
