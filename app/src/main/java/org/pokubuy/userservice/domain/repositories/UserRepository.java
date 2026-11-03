package org.pokubuy.userservice.domain.repositories;

import org.pokubuy.userservice.domain.enitites.AuthData;
import org.pokubuy.userservice.domain.enitites.UserInfo;
import org.pokubuy.userservice.domain.enitites.enums.UserRole;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface UserRepository {

    Mono<Void> createUser(
        String username,
        String email,
        String password,
        List<UserRole> roles
    );

    Mono<UserInfo> getUser(UUID id);

    Mono<String> renewToken(String refreshToken);

    Mono<AuthData> authenticateUser(String email, String password);
}
