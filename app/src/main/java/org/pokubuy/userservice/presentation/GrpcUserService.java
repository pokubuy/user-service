package org.pokubuy.userservice.presentation;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pokubuy.userservice.domain.services.UserService;
import org.pokubuy.userservice.grpc.*;
import org.pokubuy.userservice.presentation.mappers.AuthDataMapper;
import org.pokubuy.userservice.presentation.mappers.UserInfoMapper;
import org.pokubuy.userservice.presentation.mappers.UserRoleMapper;
import org.springframework.grpc.server.service.GrpcService;

import java.util.UUID;

@GrpcService
@RequiredArgsConstructor
@Slf4j
public final class GrpcUserService extends UserServiceGrpc.UserServiceImplBase {

    private final UserService userService;
    private final UserRoleMapper userRoleMapper;
    private final AuthDataMapper authDataMapper;
    private final UserInfoMapper userInfoMapper;

    @Override
    public void createUser(CreateUserRequest request, StreamObserver<AuthData> responseObserver) {
        userService.createUser(
                request.getUsername(),
                request.getEmail(),
                request.getPassword(),
                request.getRolesList()
                    .stream()
                    .map(userRoleMapper::toDomain)
                    .toList()
            )
            .map(authDataMapper::toGrpc)
            .subscribe(
                responseObserver::onNext,
                error -> {
                    log.error("error occurred", error);
                    responseObserver.onError(error);
                },
                responseObserver::onCompleted
            );
    }

    @Override
    public void authenticateUser(
        AuthenticateUserRequest request,
        StreamObserver<AuthData> responseObserver
    ) {
        userService.authenticateUser(request.getEmail(), request.getPassword())
            .map(authDataMapper::toGrpc)
            .subscribe(
                responseObserver::onNext,
                responseObserver::onError,
                responseObserver::onCompleted
            );
    }

    @Override
    public void getUser(GetUserRequest request, StreamObserver<UserInfo> responseObserver) {
        userService.getUser(UUID.fromString(request.getId()))
            .map(userInfoMapper::toGrpc)
            .subscribe(
                responseObserver::onNext,
                responseObserver::onError,
                responseObserver::onCompleted
            );
    }
}
