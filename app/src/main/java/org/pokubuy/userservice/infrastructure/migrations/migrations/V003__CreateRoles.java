package org.pokubuy.userservice.infrastructure.migrations.migrations;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.RoleRepresentation;
import org.pokubuy.userservice.infrastructure.migrations.KeycloakMigrationTask;
import org.pokubuy.userservice.infrastructure.migrations.annotations.KeycloakMigration;

import java.util.List;

@KeycloakMigration(
    version = 3,
    description = "Creates roles"
)
public final class V003__CreateRoles implements KeycloakMigrationTask {

    public final static List<String> ROLES = List.of(
        "client", "seller", "moderator", "support"
    );

    @Override
    public void execute(Keycloak keycloak, String targetRealmName) {
        var realm = keycloak.realm(targetRealmName);
        for (var strRole : ROLES) {
            var role = new RoleRepresentation();
            role.setName(strRole);
            realm.roles().create(role);
        }
    }
}
