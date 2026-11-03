package org.pokubuy.userservice.presentation;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.pokubuy.userservice.presentation.errorhandling.GrpcErrorDispatcher;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public final class ReactiveGrpcResponseHandler {

    private final GrpcErrorDispatcher errorDispatcher;

    public <T> void handle(Mono<T> mono, StreamObserver<T> responseObserver) {
        mono.subscribe(
            responseObserver::onNext,
            (error) -> errorDispatcher.onError(error, responseObserver),
            responseObserver::onCompleted
        );
    }

    public <T> void handle(Flux<T> flux, StreamObserver<T> responseObserver) {
        flux.subscribe(
            responseObserver::onNext,
            (error) -> errorDispatcher.onError(error, responseObserver),
            responseObserver::onCompleted
        );
    }
}
