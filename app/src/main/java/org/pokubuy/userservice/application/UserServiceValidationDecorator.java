package org.pokubuy.userservice.application;

import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.pokubuy.userservice.api.enitites.AuthData;
import org.pokubuy.userservice.api.enitites.UserInfo;
import org.pokubuy.userservice.api.enitites.enums.UserRole;
import org.pokubuy.userservice.api.services.UserService;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Validated
@RequiredArgsConstructor
public final class UserServiceValidationDecorator implements UserService {

    private final UserService userService;

    @Override
    public Mono<AuthData> createUser(
        String username,
        String email,
        String password,
        List<UserRole> roles
    ) {
        return createUserValidated(username, email, password, roles);
    }

    private Mono<AuthData> createUserValidated(
        @NotBlank @Size(min = 3, max = 255) String username,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8) String password,
        @NotNull @NotEmpty List<@NotNull UserRole> roles
    ) {
        return userService.createUser(username, email, password, roles);
    }

    @Override
    public Mono<AuthData> authenticateUser(
        String email,
        String password
    ) {
        return authenticateUserValidated(email, password);
    }

    private Mono<AuthData> authenticateUserValidated(
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8) String password
    ) {
        return userService.authenticateUser(email, password);
    }

    @Override
    public Mono<String> renewToken(String refreshToken) {
        return renewTokenValidated(refreshToken);
    }

    private Mono<String> renewTokenValidated(
        @NotBlank
        @Pattern(
            regexp = "^[A-Za-z0-9_-]+\\\\.[A-Za-z0-9_-]+\\\\.[A-Za-z0-9_-]+$",
            message = "Not a JWT-token"
        )
        String refreshToken
    ) {
        return userService.renewToken(refreshToken);
    }

    @Override
    public Mono<UserInfo> getUser(UUID id) {
        return getUserValidated(id);
    }

    private Mono<UserInfo> getUserValidated(@NotNull UUID id) {
        return userService.getUser(id);
    }
}
