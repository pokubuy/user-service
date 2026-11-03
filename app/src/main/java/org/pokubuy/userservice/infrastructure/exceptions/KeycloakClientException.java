package org.pokubuy.userservice.infrastructure.exceptions;

public final class KeycloakClientException extends RuntimeException {

    public KeycloakClientException(int statusCode, String message) {
        super("Keycloak client error, status code: " + statusCode + ", message: " + message);
    }
}
