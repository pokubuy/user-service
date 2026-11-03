package org.pokubuy.userservice.presentation.errorhandling;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.pokubuy.userservice.presentation.errorhandling.annotations.GrpcError;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
public final class GrpcErrorDispatcher {

    private final HashMap<Class<? extends Throwable>, GrpcErrorDefinition> map = new HashMap<>();

    public GrpcErrorDispatcher(List<GrpcErrorDefinition> errorHandlers) {
        for (var errorHandler : errorHandlers) {
            var cls = errorHandler.getClass();
            var annotation = cls.getAnnotation(GrpcError.class);
            if (annotation == null) {
                throw new IllegalStateException("All gRPC error definitions must contain @GrpcError annotation ("
                    + cls.getSimpleName()
                    + ")"
                );
            }
            var throwableCls = annotation.value();
            if (map.containsKey(throwableCls)) {
                throw new IllegalStateException(
                    "Duplicate gRPC errors (" +
                        map.get(throwableCls).getClass().getSimpleName() +
                        ", " + cls.getSimpleName() + ")"
                );
            }
            errorHandler.setThrowableClass(throwableCls);
            map.put(throwableCls, errorHandler);
        }
    }

    public void onError(Throwable error, StreamObserver<?> responseObserver) {
        var errorDefinition = map.get(error.getClass());
        if (errorDefinition == null) {
            log.error("Unknown error occurred", error);
            responseObserver.onError(Status.UNKNOWN.asRuntimeException());
            return;
        }
        log.atLevel(errorDefinition.logLevel()).log(errorDefinition.logMessage(error));
        responseObserver.onError(
            errorDefinition.grpcStatus()
                .withDescription(errorDefinition.clientMessage(error))
                .asRuntimeException()
        );
    }
}
