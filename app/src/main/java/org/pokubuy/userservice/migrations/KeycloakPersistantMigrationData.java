package org.pokubuy.userservice.migrations;

import java.time.Instant;

public record KeycloakPersistantMigrationData(
    String name,
    int version,
    String description,
    Instant timestamp
) {}
