package org.pokubuy.userservice.infrastructure.migrations.migrations;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.ClientRepresentation;
import org.pokubuy.userservice.infrastructure.migrations.KeycloakMigrationTask;
import org.pokubuy.userservice.infrastructure.migrations.annotations.KeycloakMigration;

@KeycloakMigration(
    version = 2,
    description = "Creates user-service client"
)
public final class V002__CreateUserServiceClient implements KeycloakMigrationTask {

    @Override
    public void execute(Keycloak keycloak, String targetRealmName) {
        var realm = keycloak.realm(targetRealmName);
        var client = new ClientRepresentation();
        client.setId("user-service");
        client.setName("User Service");
        client.setPublicClient(true);
        client.setDirectAccessGrantsEnabled(true);
        realm.clients().create(client).close();
    }
}
