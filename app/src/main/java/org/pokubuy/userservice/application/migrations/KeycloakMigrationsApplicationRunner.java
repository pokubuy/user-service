package org.pokubuy.userservice.application.migrations;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class KeycloakMigrationsApplicationRunner implements ApplicationRunner {

    private final KeycloakMigrationRunner migrationRunner;

    @Override
    public void run(@NonNull ApplicationArguments args) {
        migrationRunner.runMigrations().block(); // fail fast
    }
}
