package org.pokubuy.userservice.application.migrations.migrations;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.RealmRepresentation;
import org.pokubuy.userservice.application.migrations.KeycloakMigrationTask;
import org.pokubuy.userservice.application.migrations.annotations.KeycloakMigration;

@KeycloakMigration(
    version = 1,
    description = "Creates Pokubuy realm"
)
public final class V001__CreatePokubuyRealm implements KeycloakMigrationTask {

    public final static String REALM_NAME = "pokubuy";

    @Override
    public void execute(Keycloak keycloak) {
        var realm = new RealmRepresentation();
        realm.setRealm(REALM_NAME);
        realm.setEnabled(true);
        keycloak.realms().create(realm);
    }
}
