package org.pokubuy.userservice.domain.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pokubuy.userservice.api.enitites.AuthData;
import org.pokubuy.userservice.api.enitites.UserInfo;
import org.pokubuy.userservice.api.enitites.enums.UserRole;
import org.pokubuy.userservice.api.services.UserService;
import org.pokubuy.userservice.domain.exceptions.UserNotFoundException;
import org.pokubuy.userservice.domain.repositories.UserRepository;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

import static org.pokubuy.userservice.domain.utils.MaskDataUtil.maskData;

@RequiredArgsConstructor
@Slf4j
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
            .doOnSuccess(_ -> log.debug("user {} created", maskData(username)))
            .then(authenticateUser(email, password));
    }

    @Override
    public Mono<AuthData> authenticateUser(String email, String password) {
        return userRepository
            .authenticateUser(email, password)
            .doOnSuccess(_ -> log.debug("User {} authenticated", maskData(email)));
    }

    @Override
    public Mono<String> renewToken(String refreshToken) {
        return userRepository.renewToken(refreshToken)
            .doOnSuccess(_ -> log.debug("Token {} refreshed", maskData(refreshToken)));
    }


    public Mono<UserInfo> getUser(UUID id) {
        return userRepository.getUser(id)
            .doOnSuccess(userInfo -> log.debug("User {} found (username {})", id, maskData(userInfo.email())))
            .switchIfEmpty(Mono.error(() -> new UserNotFoundException(id)));
    }
}
