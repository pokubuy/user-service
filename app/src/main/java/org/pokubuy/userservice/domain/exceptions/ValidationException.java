package org.pokubuy.userservice.domain.exceptions;

public final class ValidationException extends RuntimeException {

    public ValidationException(String expectedType, String invalidData) {
        super("Validation error: expected [" + expectedType + "], got data: " + invalidData);
    }
}
