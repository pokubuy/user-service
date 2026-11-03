package org.pokubuy.userservice.domain.exceptions;

public final class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException() {
        super("User with same credentials already exists");
    }
}
