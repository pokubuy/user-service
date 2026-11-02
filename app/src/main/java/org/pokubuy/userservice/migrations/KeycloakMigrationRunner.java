package org.pokubuy.userservice.migrations;

import org.keycloak.admin.client.Keycloak;
import org.pokubuy.userservice.migrations.annotations.KeycloakMigration;
import org.springframework.boot.context.properties.bind.Name;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;

@Component
public final class KeycloakMigrationRunner {

    private final KeycloakMigrationRepository repository;
    private final TransactionalOperator transactionalOperator;
    private final Keycloak keycloak;
    private final Scheduler keycloakScheduler;
    private final List<KeycloakMigrationData> migrations;

    public KeycloakMigrationRunner(
        KeycloakMigrationRepository repository,
        TransactionalOperator transactionalOperator,
        Keycloak keycloak,
        @Name("keycloakScheduler") Scheduler keycloakScheduler,
        List<KeycloakMigrationTask> migrations
    ) {
        this.repository = repository;
        this.transactionalOperator = transactionalOperator;
        this.keycloak = keycloak;
        this.keycloakScheduler = keycloakScheduler;
        this.migrations = migrations.stream()
            .map(this::getData)
            .sorted(Comparator.comparingInt(migration -> migration.annotation().version()))
            .toList();
    }

    public Mono<Void> runMigrations() {
        return repository.lockTable()
            .then(repository.getLatestVersion())
            .flatMapMany(latestVersion -> Flux.fromIterable(migrations)
                .filter(migration -> migration.annotation().version() > latestVersion)
            )
            .concatMap(migration -> migration.task().execute(keycloak, keycloakScheduler)
                .then(repository.createMigration(new KeycloakPersistantMigrationData(
                    migration.task().getClass().getSimpleName(),
                    migration.annotation().version(),
                    migration.annotation().description(),
                    Instant.now()
                )))
            )
            .then()
            .as(transactionalOperator::transactional);
    }

    private KeycloakMigrationData getData(KeycloakMigrationTask migrationTask) {
        var annotation = AnnotationUtils.findAnnotation(migrationTask.getClass(), KeycloakMigration.class);
        if (annotation == null) {
            throw new IllegalStateException("All migration tasks must contain @KeycloakMigration annotation");
        }
        if (annotation.version() < 1) {
            throw new IllegalStateException("Migration version must be at least 1");
        }
        return new KeycloakMigrationData(annotation, migrationTask);
    }
}
