package org.pokubuy.userservice.domain.exceptions;

public final class UnauthorizedException extends RuntimeException {

    public UnauthorizedException() {
        super("Authorization error");
    }
}
