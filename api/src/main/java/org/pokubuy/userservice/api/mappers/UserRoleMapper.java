package org.pokubuy.userservice.api.mappers;

import org.pokubuy.userservice.grpc.UserRole;

public final class UserRoleMapper {

    private final static String PREFIX = "USER_ROLE_";

    public org.pokubuy.userservice.grpc.UserRole toGrpc(
        org.pokubuy.userservice.api.enitites.enums.UserRole domainUserRole
    ) {
        return org.pokubuy.userservice.grpc.UserRole.valueOf(PREFIX + domainUserRole.name());
    }

    public org.pokubuy.userservice.api.enitites.enums.UserRole toDomain(
        org.pokubuy.userservice.grpc.UserRole grpcUserRole
    ) {
        if (grpcUserRole.getNumber() == UserRole.USER_ROLE_UNSPECIFIED_VALUE) {
            throw new IllegalArgumentException("Invalid UserRole");
        }
        return org.pokubuy.userservice.api.enitites.enums.UserRole.valueOf(
            grpcUserRole.getValueDescriptor().getName().substring(PREFIX.length())
        );
    }
}
