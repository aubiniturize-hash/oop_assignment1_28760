package com.auca.exceptions;

// Thrown when a required record (e.g. Department) is missing and the user chose not to register it. Unchecked on purpose so it can travel through the form lambdas.
public class PrerequisiteMissingException extends RuntimeException {

    public PrerequisiteMissingException(String message) {
        super(message);
    }
}
