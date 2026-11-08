package org.pokubuy.userservice.presentation.errorhandling;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.pokubuy.userservice.presentation.errorhandling.annotations.GrpcError;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
public final class GrpcErrorMapper {

    private final HashMap<Class<? extends Throwable>, GrpcErrorDefinition> map = new HashMap<>();

    public GrpcErrorMapper(List<GrpcErrorDefinition> errorHandlers) {
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

    public StatusRuntimeException mapError(Throwable error) {
        var errorDefinition = map.get(error.getClass());
        if (errorDefinition == null) {
            log.error("Unknown error occurred", error);
            return Status.UNKNOWN.asRuntimeException();
        }

        log.atLevel(errorDefinition.logLevel()).log(errorDefinition.logMessage(error));
        return errorDefinition.grpcStatus()
            .withDescription(errorDefinition.clientMessage(error))
            .asRuntimeException();
    }
}
