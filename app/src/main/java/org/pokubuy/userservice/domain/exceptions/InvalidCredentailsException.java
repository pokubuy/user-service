package org.pokubuy.userservice.domain.exceptions;

public final class InvalidCredentailsException extends RuntimeException {

    public InvalidCredentailsException(String email) {
        super("Invalid credentials for user with email " + email);
    }
}
