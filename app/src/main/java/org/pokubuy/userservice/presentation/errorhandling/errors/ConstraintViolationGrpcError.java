package org.pokubuy.userservice.presentation.errorhandling.errors;

import io.grpc.Status;
import jakarta.validation.ConstraintViolationException;
import org.pokubuy.userservice.presentation.errorhandling.GrpcErrorDefinition;
import org.pokubuy.userservice.presentation.errorhandling.annotations.GrpcError;
import org.slf4j.event.Level;

@GrpcError(ConstraintViolationException.class)
public final class ConstraintViolationGrpcError extends GrpcErrorDefinition {

    @Override
    public Status grpcStatus() {
        return Status.INVALID_ARGUMENT;
    }

    @Override
    public Level logLevel() {
        return Level.DEBUG;
    }
}
