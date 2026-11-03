package org.pokubuy.userservice.infrastructure.migrations.migrations;

import org.keycloak.admin.client.Keycloak;
import org.pokubuy.userservice.infrastructure.migrations.KeycloakMigrationTask;
import org.pokubuy.userservice.infrastructure.migrations.annotations.KeycloakMigration;

@KeycloakMigration(
    version = 4,
    description = "Makes first name and last name optional"
)
public final class V004__MakeFirstAndLastNameOptional implements KeycloakMigrationTask {

    @Override
    public void execute(Keycloak keycloak, String targetRealmName) {
        var realm = keycloak.realm(targetRealmName);
        var userProfile = realm.users().userProfile();
        var configuration = userProfile.getConfiguration();
        configuration.getAttribute("firstName").setRequired(null);
        configuration.getAttribute("lastName").setRequired(null);
        userProfile.update(configuration);
    }
}
