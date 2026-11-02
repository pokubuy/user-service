package org.pokubuy.userservice.presentation.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class UserInfoMapper {

    private UserRoleMapper userRoleMapper;

    public org.pokubuy.userservice.grpc.UserInfo toGrpc(
        org.pokubuy.userservice.domain.enitites.UserInfo domainUserInfo
    ) {
        return org.pokubuy.userservice.grpc.UserInfo.newBuilder()
            .setUsername(domainUserInfo.username())
            .setEmail(domainUserInfo.email())
            .addAllRoles(domainUserInfo.roles()
                .stream()
                .map(userRoleMapper::toGrpc).toList()
            )
            .build();
    }

    public org.pokubuy.userservice.domain.enitites.UserInfo toDomain(
        org.pokubuy.userservice.grpc.UserInfo grpcUserInfo
    ) {
        return new org.pokubuy.userservice.domain.enitites.UserInfo(
            grpcUserInfo.getUsername(),
            grpcUserInfo.getEmail(),
            grpcUserInfo.getRolesList()
                .stream()
                .map(userRoleMapper::toDomain)
                .toList()
        );
    }
}
