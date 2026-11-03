package org.pokubuy.userservice.presentation.errorhandling.errors;

import io.grpc.Status;
import org.pokubuy.userservice.domain.exceptions.UserAlreadyExistsException;
import org.pokubuy.userservice.presentation.errorhandling.GrpcErrorDefinition;
import org.pokubuy.userservice.presentation.errorhandling.annotations.GrpcError;
import org.slf4j.event.Level;

@GrpcError(UserAlreadyExistsException.class)
public final class UserAlreadyExistsGrpcErrorDefinition extends GrpcErrorDefinition {

    @Override
    public Status grpcStatus() {
        return Status.ALREADY_EXISTS;
    }

    @Override
    public Level logLevel() {
        return Level.DEBUG;
    }
}
