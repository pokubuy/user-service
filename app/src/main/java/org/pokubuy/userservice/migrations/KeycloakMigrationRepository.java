package org.pokubuy.userservice.migrations;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class KeycloakMigrationRepository {

    private final R2dbcEntityTemplate template;
    private final static String TABLE_NAME = "keycloak_migrations";

    public Mono<Integer> getLatestVersion() {
        return template.select(Integer.class)
            .from(TABLE_NAME)
            .matching(Query.empty()
                .columns("version")
                .sort(Sort.by(Sort.Direction.DESC, "version"))
                .limit(1)
            )
            .one();
    }

    public Mono<Void> lockTable() {
        return template.getDatabaseClient()
            .sql("LOCK TABLE :table_name IN ACCESS EXCLUSIVE MODE")
            .bind("table_name", TABLE_NAME)
            .then();
    }

    public Mono<Void> createMigration(KeycloakPersistentMigrationData migrationData) {
        return template.insert(KeycloakPersistentMigrationData.class)
            .into(TABLE_NAME)
            .using(migrationData)
            .then();
    }
}
