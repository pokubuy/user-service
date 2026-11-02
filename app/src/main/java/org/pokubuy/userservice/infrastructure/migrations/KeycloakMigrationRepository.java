package org.pokubuy.userservice.infrastructure.migrations;

import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class KeycloakMigrationRepository {

    private final R2dbcEntityTemplate template;
    private final static String TABLE_NAME = "keycloak_migrations";

    public Mono<Integer> getLatestVersion() {
        return template.getDatabaseClient()
            .sql("SELECT version FROM " + TABLE_NAME + " ORDER BY version DESC LIMIT 1")
            .map(row -> Objects.requireNonNull(row.get("version", Integer.class)))
            .one()
            .defaultIfEmpty(0);
    }

    public Mono<Void> lockTable() {
        return template.getDatabaseClient()
            .sql("LOCK TABLE " + TABLE_NAME + " IN ACCESS EXCLUSIVE MODE")
            .then();
    }

    public Mono<Void> createMigration(KeycloakPersistentMigrationData migrationData) {
        return template.insert(KeycloakPersistentMigrationData.class)
            .into(TABLE_NAME)
            .using(migrationData)
            .then();
    }
}
