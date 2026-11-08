package org.pokubuy.userservice.presentation.mappers;

import org.springframework.stereotype.Component;

@Component
public final class AuthDataMapper {

    public org.pokubuy.userservice.grpc.AuthData toGrpc(
        org.pokubuy.userservice.api.enitites.AuthData domainAuthData
    ) {
        return org.pokubuy.userservice.grpc.AuthData.newBuilder()
            .setAccessToken(domainAuthData.accessToken())
            .setRefreshToken(domainAuthData.refreshToken())
            .build();
    }

    public org.pokubuy.userservice.api.enitites.AuthData toDomain(
        org.pokubuy.userservice.grpc.AuthData grpcAuthData
    ) {
        return new org.pokubuy.userservice.api.enitites.AuthData(
            grpcAuthData.getAccessToken(),
            grpcAuthData.getRefreshToken()
        );
    }
}
