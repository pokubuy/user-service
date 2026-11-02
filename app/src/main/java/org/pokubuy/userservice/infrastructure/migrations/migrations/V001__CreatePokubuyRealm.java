package org.pokubuy.userservice.infrastructure.migrations.migrations;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.RealmRepresentation;
import org.pokubuy.userservice.infrastructure.migrations.KeycloakMigrationTask;
import org.pokubuy.userservice.infrastructure.migrations.annotations.KeycloakMigration;

@KeycloakMigration(
    version = 1,
    description = "Creates Pokubuy realm"
)
public final class V001__CreatePokubuyRealm implements KeycloakMigrationTask {

    @Override
    public void execute(Keycloak keycloak, String targetRealmName) {
        var realm = new RealmRepresentation();
        realm.setRealm(targetRealmName);
        realm.setEnabled(true);
        realm.setLoginWithEmailAllowed(true);
        keycloak.realms().create(realm);
    }
}
