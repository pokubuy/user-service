package org.pokubuy.userservice.application.migrations;

import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.pokubuy.userservice.application.migrations.annotations.KeycloakMigration;
import org.pokubuy.userservice.infrastructure.migrations.KeycloakMigrationRepository;
import org.pokubuy.userservice.infrastructure.migrations.KeycloakPersistentMigrationData;
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
@Slf4j
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
        log.info("Initializing migrations (total amount: {})", migrations.size());
        return repository.lockTable()
            .then(repository.getLatestVersion())
            .flatMapMany(latestVersion -> {
                log.info("Current Keycloak version is {}", latestVersion);
                return Flux.fromIterable(migrations)
                    .filter(migration -> migration.annotation().version() > latestVersion);
            })
            .collectList()
            .flatMapMany(migrations -> {
                if (migrations.isEmpty()) {
                    log.info("No migrations applicable. Skipping migrations");
                    return Mono.just(false);
                }
                log.info("Applying {} migrations", migrations.size());
                return Flux.fromIterable(migrations)
                    .concatMap(migration -> {
                            log.info(
                                "Applying migration version {}: {}",
                                migration.annotation().version(),
                                migration.task().getClass().getSimpleName()
                            );
                            return Mono.fromRunnable(() -> migration.task().execute(keycloak))
                                .subscribeOn(keycloakScheduler)
                                .then(repository.createMigration(new KeycloakPersistentMigrationData(
                                    migration.task().getClass().getSimpleName(),
                                    migration.annotation().version(),
                                    migration.annotation().description(),
                                    Instant.now()
                                )));
                        }
                    )
                    .then(Mono.just(true));
            })
            .as(transactionalOperator::transactional)
            .doOnNext(applied -> {
                if (applied) {
                    log.info("Migrations completed");
                }
            })
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
