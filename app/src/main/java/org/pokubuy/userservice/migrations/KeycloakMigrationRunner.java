package org.pokubuy.userservice.migrations;

import org.pokubuy.userservice.migrations.annotations.KeycloakMigration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;

@Component
public final class KeycloakMigrationRunner {

    private final KeycloakMigrationRepository repository;
    private final List<KeycloakMigrationData> migrations;

    public KeycloakMigrationRunner(
        KeycloakMigrationRepository repository,
        List<KeycloakMigrationTask> migrations
    ) {
        this.repository = repository;
        this.migrations = migrations.stream()
            .map(this::getData)
            .sorted(Comparator.comparingInt(migration -> migration.annotation().version()))
            .toList();
    }

    public Mono<Void> runMigrations() {
        return repository.getLatestVersion()
            .flatMapMany(latestVersion -> Flux.fromIterable(migrations)
                .filter(migration -> migration.annotation().version() > latestVersion)
            )
            .concatMap(migration -> migration.task().execute(this))
            .then();
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
