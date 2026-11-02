package org.pokubuy.userservice.application.migrations;

import org.pokubuy.userservice.application.migrations.annotations.KeycloakMigration;

public record KeycloakMigrationData(
    KeycloakMigration annotation,
    KeycloakMigrationTask task
) {}
