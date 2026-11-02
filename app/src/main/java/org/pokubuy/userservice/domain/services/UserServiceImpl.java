package org.pokubuy.userservice.domain.services;

import lombok.RequiredArgsConstructor;
import org.pokubuy.userservice.domain.enitites.AuthData;
import org.pokubuy.userservice.domain.enitites.UserInfo;
import org.pokubuy.userservice.domain.enitites.enums.UserRole;
import org.pokubuy.userservice.domain.exceptions.UserNotFoundException;
import org.pokubuy.userservice.domain.repositories.UserRepository;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public final class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public Mono<AuthData> createUser(
        String username,
        String email,
        String password,
        List<UserRole> roles
    ) {
        return userRepository
            .createUser(username, email, password, roles)
            .then(authenticateUser(email, password));
    }

    @Override
    public Mono<AuthData> authenticateUser(String email, String password) {
        return userRepository.authenticateUser(email, password);
    }


    public Mono<UserInfo> getUser(UUID id) {
        return userRepository.getUser(id)
            .switchIfEmpty(Mono.error(() -> new UserNotFoundException(id)));
    }
}
