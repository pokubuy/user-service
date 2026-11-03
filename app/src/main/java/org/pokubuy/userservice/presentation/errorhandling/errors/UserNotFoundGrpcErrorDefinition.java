package org.pokubuy.userservice.presentation.errorhandling.errors;

import io.grpc.Status;
import org.pokubuy.userservice.domain.exceptions.UserNotFoundException;
import org.pokubuy.userservice.presentation.errorhandling.GrpcErrorDefinition;
import org.pokubuy.userservice.presentation.errorhandling.annotations.GrpcError;
import org.slf4j.event.Level;

@GrpcError(UserNotFoundException.class)
public final class UserNotFoundGrpcErrorDefinition extends GrpcErrorDefinition {

    @Override
    public Status grpcStatus() {
        return Status.NOT_FOUND;
    }

    @Override
    public Level logLevel() {
        return Level.DEBUG;
    }
}
