package org.pokubuy.userservice.migrations;

import org.pokubuy.userservice.migrations.annotations.KeycloakMigration;

public record KeycloakMigrationData(
    KeycloakMigration annotation,
    KeycloakMigrationTask task
) {}
