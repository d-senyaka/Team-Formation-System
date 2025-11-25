package com.teame.exception;

import java.util.List;

/**
 * Custom exception used to signal validation failures when reading
 * or processing participant data.
 */
public class ValidationException extends RuntimeException {

    private final List<String> errorMessages;

    public ValidationException(String message) {
        super(message);
        this.errorMessages = List.of(message);
    }

    public ValidationException(List<String> errorMessages) {
        super(String.join("; ", errorMessages));
        this.errorMessages = errorMessages;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }
}
