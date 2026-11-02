package org.pokubuy.userservice.infrastructure.migrations;

import org.pokubuy.userservice.infrastructure.migrations.annotations.KeycloakMigration;

public record KeycloakMigrationData(
    KeycloakMigration annotation,
    KeycloakMigrationTask task
) {}
