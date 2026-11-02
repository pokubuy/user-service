package org.pokubuy.userservice.domain.services;

import org.pokubuy.userservice.domain.enitites.AuthData;
import org.pokubuy.userservice.domain.enitites.UserInfo;
import org.pokubuy.userservice.domain.enitites.enums.UserRole;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface UserService {

    Mono<AuthData> createUser(
        String username,
        String email,
        String password,
        List<UserRole> roles
    );

    Mono<AuthData> authenticateUser(
        String email,
        String password
    );

    Mono<UserInfo> getUser(UUID id);
}
