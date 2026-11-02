package org.pokubuy.userservice.domain.exceptions;

import java.util.UUID;

public final class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(UUID id) {
        super("User " + id.toString() + " not found");
    }
}
