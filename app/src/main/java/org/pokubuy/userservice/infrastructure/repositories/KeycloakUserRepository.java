package org.pokubuy.userservice.infrastructure.repositories;

import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.NotAuthorizedException;
import jdk.jfr.Name;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.pokubuy.userservice.application.configuration.configurationproperties.KeycloakConfigurationProperties;
import org.pokubuy.userservice.domain.enitites.AuthData;
import org.pokubuy.userservice.domain.enitites.UserInfo;
import org.pokubuy.userservice.domain.enitites.enums.UserRole;
import org.pokubuy.userservice.domain.exceptions.InvalidCredentailsException;
import org.pokubuy.userservice.domain.exceptions.UserAlreadyExistsException;
import org.pokubuy.userservice.domain.repositories.UserRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

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

    @PostConstruct
    private void init() {
        targetRealm = keycloakAdmin.realm(keycloakConfigurationProperties.getTargetRealmName());
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
            if (status != 200) {
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
        return execute(() -> {
            try (var client = createAuthClient(email, password)) {
                var token = client.tokenManager().getAccessToken();
                return new AuthData(
                    token.getToken(),
                    token.getRefreshToken()
                );
            } catch (NotAuthorizedException ex) {
                throw new InvalidCredentailsException(email);
            }
        });
    }

    @Override
    public Mono<UserInfo> getUser(UUID id) {
        return execute(() -> targetRealm.users().get(id.toString()))
            .map(userResource -> {
                var userRepresentation = userResource.toRepresentation();
                return new UserInfo(
                    userRepresentation.getUsername(),
                    userRepresentation.getEmail(),
                    mapRoles(userRepresentation.getRealmRoles())
                );
            });
    }

    private Keycloak createAuthClient(String email, String password) {
        return KeycloakBuilder.builder()
            .serverUrl(keycloakConfigurationProperties.getUrl())
            .realm(keycloakConfigurationProperties.getTargetRealmName())
            .clientId(keycloakConfigurationProperties.getTargetClientId())
            .grantType(OAuth2Constants.PASSWORD)
            .username(email)
            .password(password)
            .build();
    }

    private static List<UserRole> mapRoles(List<String> strRoles) {
        return strRoles.stream()
            .map(strRole -> UserRole.valueOf(strRole.toUpperCase(Locale.ROOT)))
            .toList();
    }

    private <T> Mono<T> execute(Callable<T> callable) {
        return Mono.fromCallable(callable).subscribeOn(keycloakScheduler);
    }

    private Mono<Void> execute(Runnable runnable) {
        return Mono.fromRunnable(runnable).subscribeOn(keycloakScheduler).then();
    }
}
