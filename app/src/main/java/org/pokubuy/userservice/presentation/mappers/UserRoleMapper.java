package org.pokubuy.userservice.presentation.mappers;

import org.springframework.stereotype.Component;

@Component
public final class UserRoleMapper {

    private final static String PREFIX = "USER_ROLE_";

    public org.pokubuy.userservice.grpc.UserRole toGrpc(
        org.pokubuy.userservice.domain.enitites.enums.UserRole domainUserRole
    ) {
        return org.pokubuy.userservice.grpc.UserRole.valueOf(PREFIX + domainUserRole.name());
    }

    public org.pokubuy.userservice.domain.enitites.enums.UserRole toDomain(
        org.pokubuy.userservice.grpc.UserRole grpcUserRole
    ) {
        return org.pokubuy.userservice.domain.enitites.enums.UserRole.valueOf(
            grpcUserRole.getValueDescriptor().getName().substring(PREFIX.length())
        );
    }
}
