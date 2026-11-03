package org.pokubuy.userservice.presentation.errorhandling.errors;

import io.grpc.Status;
import org.pokubuy.userservice.infrastructure.exceptions.KeycloakClientException;
import org.pokubuy.userservice.presentation.errorhandling.GrpcErrorDefinition;
import org.pokubuy.userservice.presentation.errorhandling.annotations.GrpcError;
import org.slf4j.event.Level;

@GrpcError(KeycloakClientException.class)
public final class KeycloakClientGrpcErrorDefinition extends GrpcErrorDefinition {

    @Override
    public Status grpcStatus() {
        return Status.INTERNAL;
    }

    @Override
    public Level logLevel() {
        return Level.ERROR;
    }
}
