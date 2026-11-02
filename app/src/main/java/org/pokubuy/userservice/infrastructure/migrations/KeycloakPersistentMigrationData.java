package org.pokubuy.userservice.infrastructure.migrations;

import java.time.Instant;

public record KeycloakPersistentMigrationData(
    String name,
    int version,
    String description,
    Instant timestamp
) {}
