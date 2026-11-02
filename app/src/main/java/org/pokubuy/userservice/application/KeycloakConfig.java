package org.pokubuy.userservice.application;

import org.keycloak.admin.client.Keycloak;
import org.pokubuy.userservice.application.configurationproperties.KeycloakConfigurationProperties;
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
        return Keycloak.getInstance(
            configurationProperties.getUrl(),
            configurationProperties.getRealm(),
            configurationProperties.getUsername(),
            configurationProperties.getPassword()
        );
    }
}
