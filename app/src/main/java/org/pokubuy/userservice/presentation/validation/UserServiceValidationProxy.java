package org.pokubuy.userservice.presentation.validation;

import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.pokubuy.userservice.api.enitites.AuthData;
import org.pokubuy.userservice.api.enitites.UserInfo;
import org.pokubuy.userservice.api.enitites.enums.UserRole;
import org.pokubuy.userservice.api.services.UserService;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Component
@Validated
@RequiredArgsConstructor
public class UserServiceValidationProxy {

    private final UserService userService;

    public Mono<AuthData> createUser(
        @NotBlank @Size(min = 3, max = 255) String username,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8) String password,
        @NotNull @NotEmpty List<@NotNull UserRole> roles
    ) {
        return userService.createUser(username, email, password, roles);
    }

    public Mono<AuthData> authenticateUser(
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8) String password
    ) {
        return userService.authenticateUser(email, password);
    }

    public Mono<String> renewToken(
        @NotBlank
        @Pattern(
            regexp = "^[A-Za-z0-9_-]+\\.[A-Za-z0-9_-]+\\.[A-Za-z0-9_-]+$",
            message = "Not a JWT-token"
        )
        String refreshToken
    ) {
        return userService.renewToken(refreshToken);
    }

    public Mono<UserInfo> getUser(@NotNull UUID id) {
        return userService.getUser(id);
    }
}
