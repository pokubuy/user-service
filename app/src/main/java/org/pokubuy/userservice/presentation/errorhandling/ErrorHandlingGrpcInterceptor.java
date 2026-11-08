package org.pokubuy.userservice.presentation.errorhandling;

import io.grpc.*;
import lombok.RequiredArgsConstructor;
import org.springframework.grpc.server.GlobalServerInterceptor;
import org.springframework.stereotype.Component;

@GlobalServerInterceptor
@Component
@RequiredArgsConstructor
public final class ErrorHandlingGrpcInterceptor implements ServerInterceptor {

    private final GrpcErrorMapper grpcErrorMapper;

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
        ServerCall<ReqT, RespT> call,
        Metadata headers,
        ServerCallHandler<ReqT, RespT> next
    ) {
        var errorHandlingCall = new ForwardingServerCall.SimpleForwardingServerCall<>(call) {
            @Override
            public void close(Status status, Metadata trailers) {
                if (!status.isOk() && status.getCause() != null) {
                    var error = grpcErrorMapper.mapError(status.getCause());
                    super.close(error.getStatus(), (error.getTrailers() != null) ? error.getTrailers() : trailers);
                } else {
                    super.close(status, trailers);
                }
            }
        };
        return next.startCall(errorHandlingCall, headers);
    }
}
