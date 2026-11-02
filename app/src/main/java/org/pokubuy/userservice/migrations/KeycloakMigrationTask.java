package org.pokubuy.userservice.migrations;

import reactor.core.publisher.Mono;

@FunctionalInterface
public interface KeycloakMigrationTask {

    Mono<Void> execute(KeycloakMigrationRunner migrationRunner);
}
