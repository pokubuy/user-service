package org.pokubuy.userservice.application.migrations.migrations;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.GroupRepresentation;
import org.pokubuy.userservice.application.migrations.KeycloakMigrationTask;
import org.pokubuy.userservice.application.migrations.annotations.KeycloakMigration;

import java.util.Set;

@KeycloakMigration(
    version = 2,
    description = "Creates roles and groups"
)
public final class V002__CreateGroups implements KeycloakMigrationTask {

    public final static Set<String> GROUPS = Set.of("user", "admin");

    @Override
    public void execute(Keycloak keycloak) {
        var realm = keycloak.realm(V001__CreatePokubuyRealm.REALM_NAME);
        for (var strGroup : GROUPS) {
            var group = new GroupRepresentation();
            group.setName(strGroup);
            realm.groups().add(group).close();
        }
    }
}
