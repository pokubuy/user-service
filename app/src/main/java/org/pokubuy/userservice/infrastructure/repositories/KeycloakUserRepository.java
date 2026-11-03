package org.pokubuy.userservice.infrastructure.repositories;

import jakarta.annotation.PostConstruct;
import jdk.jfr.Name;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.pokubuy.userservice.application.configuration.configurationproperties.KeycloakConfigurationProperties;
import org.pokubuy.userservice.domain.enitites.AuthData;
import org.pokubuy.userservice.domain.enitites.UserInfo;
import org.pokubuy.userservice.domain.enitites.enums.UserRole;
import org.pokubuy.userservice.domain.exceptions.UserAlreadyExistsException;
import org.pokubuy.userservice.domain.repositories.UserRepository;
import org.pokubuy.userservice.infrastructure.repositories.entities.RawAccessTokenData;
import org.pokubuy.userservice.infrastructure.repositories.entities.RawAuthData;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.Callable;

@Repository
@RequiredArgsConstructor
@Slf4j
public class KeycloakUserRepository implements UserRepository {

    private final Keycloak keycloakAdmin;

    @Name("keycloakScheduler")
    private final Scheduler keycloakScheduler;

    private final KeycloakConfigurationProperties keycloakConfigurationProperties;

    private RealmResource targetRealm;

    private String authUrl;

    private final WebClient webClient;

    @PostConstruct
    private void init() {
        targetRealm = keycloakAdmin.realm(keycloakConfigurationProperties.getTargetRealmName());
        authUrl = keycloakConfigurationProperties.getUrl() +
            "/realms/" +
            keycloakConfigurationProperties.getTargetRealmName() +
            "/protocol/openid-connect/token";
    }

    @Override
    public Mono<Void> createUser(
        String username,
        String email,
        String password,
        List<UserRole> roles
    ) {
        return execute(() -> {
            var user = new UserRepresentation();
            user.setUsername(username);
            user.setEmail(email);
            user.setEmailVerified(true);

            var credential = new CredentialRepresentation();
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(password);
            credential.setTemporary(false);
            user.setCredentials(List.of(credential));

            user.setEnabled(true);

            var response = targetRealm.users().create(user);
            var status = response.getStatus();
            if (!isSuccess(status)) {
                if (status == 409) {
                    throw new UserAlreadyExistsException();
                } else {
                    throw new RuntimeException("Keycloak client error, status code: " + status);
                }
            }
            var location = response.getHeaderString("Location");
            String userId = location.substring(location.lastIndexOf('/') + 1);
            response.close();

            var userResource = targetRealm.users().get(userId);
            log.info("required actions {}", userResource.toRepresentation().getRequiredActions());
            var strRoles = roles.stream()
                .map(role -> role.toString().toLowerCase(Locale.ROOT))
                .toList();

            var roleRepresentations = strRoles.stream()
                .map(strRole -> targetRealm.roles().get(strRole).toRepresentation())
                .toList();

            userResource.roles().realmLevel().add(roleRepresentations);
        });
    }

    @Override
    public Mono<AuthData> authenticateUser(String email, String password) {
        return Mono.fromCallable(() -> {
                MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
                formData.add("grant_type", "password");
                formData.add("client_id", keycloakConfigurationProperties.getTargetClientId());
                formData.add("username", email);
                formData.add("password", password);
                return formData;
            }).flatMap(formData -> webClient.post()
                .uri(authUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(formData)
                .retrieve()
                .bodyToMono(RawAuthData.class))
            .map(RawAuthData::toAuthData)
            .flatMap(authData -> execute(() -> {
                var userResource = targetRealm.users()
                    .get(targetRealm.users().searchByEmail(email, true).getFirst().getId());

                var sessions = userResource.getOfflineSessions(keycloakConfigurationProperties.getTargetClientId());
                if (sessions.size() > keycloakConfigurationProperties.getMaxSessions()) {
                    var counter = 0;
                    var targetRemovedSessionsAmount = sessions.size() - keycloakConfigurationProperties.getMaxSessions();
                    for (var session : sessions) {
                        targetRealm.deleteSession(session.getId(), true);
                        if (++counter >= targetRemovedSessionsAmount) {
                            break;
                        }
                    }
                }
            }).thenReturn(authData));
    }

    @Override
    public Mono<UserInfo> getUser(UUID id) {
        return execute(() -> targetRealm.users().get(id.toString()))
            .map(userResource -> {
                var userRepresentation = userResource.toRepresentation();
                return new UserInfo(
                    userRepresentation.getUsername(),
                    userRepresentation.getEmail(),
                    mapRoleRepresentations(userResource.roles().realmLevel().listAll())
                );
            });
    }

    @Override
    public Mono<String> renewToken(String refreshToken) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "refresh_token");
        formData.add("client_id", keycloakConfigurationProperties.getTargetClientId());
        formData.add("refresh_token", refreshToken);

        return webClient.post()
            .uri(authUrl)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .bodyValue(formData)
            .retrieve()
            .bodyToMono(RawAccessTokenData.class)
            .map(RawAccessTokenData::accessToken);
    }

    private static List<UserRole> mapRoleRepresentations(List<RoleRepresentation> rolesRepresentations) {
        return mapRoles(
            rolesRepresentations.stream().map(RoleRepresentation::getName).toList()
        );
    }

    private static List<UserRole> mapRoles(List<String> strRoles) {
        return Arrays.stream(UserRole.values()).filter(
            role -> strRoles.contains(role.toString().toLowerCase(Locale.ROOT))
        ).toList();
    }

    private <T> Mono<T> execute(Callable<T> callable) {
        return Mono.fromCallable(callable).subscribeOn(keycloakScheduler);
    }

    private Mono<Void> execute(Runnable runnable) {
        return Mono.fromRunnable(runnable).subscribeOn(keycloakScheduler).then();
    }

    private boolean isSuccess(int status) {
        return status / 100 == 2;
    }
}
