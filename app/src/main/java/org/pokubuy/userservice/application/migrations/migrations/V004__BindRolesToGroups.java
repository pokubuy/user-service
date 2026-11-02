package org.pokubuy.userservice.application.migrations.migrations;

import org.keycloak.admin.client.Keycloak;
import org.pokubuy.userservice.application.migrations.KeycloakMigrationTask;
import org.pokubuy.userservice.application.migrations.annotations.KeycloakMigration;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@KeycloakMigration(
    version = 4,
    description = "Binds roles to groups"
)
public final class V004__BindRolesToGroups implements KeycloakMigrationTask {

    private final static Map<String, List<String>> GROUP_ROLES = Map.of(
        "user", List.of("client", "seller"),
        "admin", List.of("moderator", "support")
    );

    @Override
    public void execute(Keycloak keycloak) {
        var realm = keycloak.realm(V001__CreatePokubuyRealm.REALM_NAME);
        var groups = realm.groups().groups();
        for (var kv : GROUP_ROLES.entrySet()) {
            var group = groups.stream()
                .filter(g -> Objects.equals(g.getName(), kv.getKey()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Group not found: " + kv.getKey()));

            var roles = kv.getValue().stream()
                .map(strRole -> realm.roles().get(strRole).toRepresentation())
                .toList();

            realm.groups().group(group.getId())
                .roles()
                .realmLevel()
                .add(roles);
        }
    }
}
