package org.pokubuy.userservice.presentation.errorhandling.errors;

import io.grpc.Status;
import org.pokubuy.userservice.domain.exceptions.InvalidCredentailsException;
import org.pokubuy.userservice.presentation.errorhandling.GrpcErrorDefinition;
import org.pokubuy.userservice.presentation.errorhandling.annotations.GrpcError;
import org.slf4j.event.Level;

@GrpcError(InvalidCredentailsException.class)
public final class InvalidCredentialsGrpcErrorDefinition extends GrpcErrorDefinition {

    @Override
    public Status grpcStatus() {
        return Status.UNAUTHENTICATED;
    }

    @Override
    public Level logLevel() {
        return Level.DEBUG;
    }
}
