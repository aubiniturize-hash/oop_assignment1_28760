package com.auca.exceptions;

// Thrown when a search or update cannot find the requested record.
public class RecordNotFoundException extends Exception {

    public RecordNotFoundException(String message) {
        super(message);
    }
}
