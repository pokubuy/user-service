package org.pokubuy.userservice.presentation.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pokubuy.userservice.api.services.UserService;
import org.pokubuy.userservice.grpc.*;
import org.pokubuy.userservice.presentation.errorhandling.GrpcErrorMapper;
import org.pokubuy.userservice.presentation.mappers.AuthDataMapper;
import org.pokubuy.userservice.presentation.mappers.UserInfoMapper;
import org.pokubuy.userservice.presentation.mappers.UserRoleMapper;
import org.springframework.grpc.server.service.GrpcService;
import reactor.core.publisher.Mono;

import java.util.UUID;

@GrpcService
@RequiredArgsConstructor
@Slf4j
public final class GrpcUserService extends ReactorUserServiceGrpc.UserServiceImplBase {

    private final UserService userService;
    private final UserRoleMapper userRoleMapper;
    private final AuthDataMapper authDataMapper;
    private final UserInfoMapper userInfoMapper;
    private final GrpcErrorMapper grpcErrorMapper;

    @Override
    public Mono<AuthData> createUser(CreateUserRequest request) {
        return userService.createUser(
                request.getUsername(),
                request.getEmail(),
                request.getPassword(),
                request.getRolesList()
                    .stream()
                    .map(userRoleMapper::toDomain)
                    .toList()
            )
            .map(authDataMapper::toGrpc)
            .onErrorMap(grpcErrorMapper::mapError);
    }

    @Override
    public Mono<AuthData> authenticateUser(AuthenticateUserRequest request) {
        return userService.authenticateUser(request.getEmail(), request.getPassword())
            .map(authDataMapper::toGrpc)
            .onErrorMap(grpcErrorMapper::mapError);
    }

    @Override
    public Mono<RenewTokenResponse> renewToken(RenewTokenRequest request) {
        return userService.renewToken(request.getRefreshToken())
            .map(token -> RenewTokenResponse.newBuilder().setAccessToken(token).build())
            .onErrorMap(grpcErrorMapper::mapError);
    }

    @Override
    public Mono<UserInfo> getUser(GetUserRequest request) {
        return userService.getUser(UUID.fromString(request.getId()))
            .map(userInfoMapper::toGrpc)
            .onErrorMap(grpcErrorMapper::mapError);
    }
}
