package org.pokubuy.userservice.migrations;

import org.keycloak.admin.client.Keycloak;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

@FunctionalInterface
public interface KeycloakMigrationTask {

    Mono<Void> execute(Keycloak keycloak, Scheduler keycloakScheduler);
}
