package org.pokubuy.userservice.migrations.migrations;

import org.pokubuy.userservice.migrations.KeycloakMigrationRunner;
import org.pokubuy.userservice.migrations.KeycloakMigrationTask;
import org.pokubuy.userservice.migrations.annotations.KeycloakMigration;
import reactor.core.publisher.Mono;

@KeycloakMigration(
    version = 1,
    description = "Creates pokubuy realm"
)
public final class V001__CreatePokubuyRealm implements KeycloakMigrationTask {

    @Override
    public Mono<Void> execute(KeycloakMigrationRunner migrationRunner) {
        return Mono.empty();
    }
}
