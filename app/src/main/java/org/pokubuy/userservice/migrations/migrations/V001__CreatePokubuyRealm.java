package org.pokubuy.userservice.migrations.migrations;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.RealmRepresentation;
import org.pokubuy.userservice.migrations.KeycloakMigrationTask;
import org.pokubuy.userservice.migrations.annotations.KeycloakMigration;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

@KeycloakMigration(
    version = 1,
    description = "Creates Pokubuy realm"
)
public final class V001__CreatePokubuyRealm implements KeycloakMigrationTask {

    private final static String REALM_NAME = "pokubuy";

    @Override
    public Mono<Void> execute(Keycloak keycloak, Scheduler keycloakScheduler) {
        var realm = new RealmRepresentation();
        realm.setRealm(REALM_NAME);
        realm.setEnabled(true);

        return Mono.fromRunnable(() -> keycloak.realms().create(realm))
            .subscribeOn(keycloakScheduler)
            .then();
    }
}
