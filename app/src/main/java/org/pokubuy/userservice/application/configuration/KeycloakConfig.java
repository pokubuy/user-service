package org.pokubuy.userservice.application.configuration;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.pokubuy.userservice.application.configuration.configurationproperties.KeycloakConfigurationProperties;
import org.springframework.boot.context.properties.bind.Name;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.ExecutorService;

@Configuration
public class KeycloakConfig {

    @Bean(name = "keycloakScheduler")
    public Scheduler keycloakScheduler(
        @Name("virtualThreadExecutorService")
        ExecutorService virtualThreadExecutorService
    ) {
        return Schedulers.fromExecutorService(virtualThreadExecutorService);
    }

    @Bean
    public Keycloak keycloak(KeycloakConfigurationProperties configurationProperties) {
        return KeycloakBuilder.builder()
            .serverUrl(configurationProperties.getUrl())
            .realm(configurationProperties.getRealm())
            .username(configurationProperties.getUsername())
            .password(configurationProperties.getPassword())
            .clientId(configurationProperties.getClientId())
            .build();
    }
}
