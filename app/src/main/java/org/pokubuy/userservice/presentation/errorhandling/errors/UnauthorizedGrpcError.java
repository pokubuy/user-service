package org.pokubuy.userservice.presentation.errorhandling.errors;

import io.grpc.Status;
import org.pokubuy.userservice.domain.exceptions.UnauthorizedException;
import org.pokubuy.userservice.presentation.errorhandling.GrpcErrorDefinition;
import org.pokubuy.userservice.presentation.errorhandling.annotations.GrpcError;
import org.slf4j.event.Level;

@GrpcError(UnauthorizedException.class)
public final class UnauthorizedGrpcError extends GrpcErrorDefinition {

    @Override
    public Status grpcStatus() {
        return Status.UNAUTHENTICATED;
    }

    @Override
    public Level logLevel() {
        return Level.DEBUG;
    }
}
