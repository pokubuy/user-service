package org.pokubuy.userservice.migrations;

import org.keycloak.admin.client.Keycloak;

@FunctionalInterface
public interface KeycloakMigrationTask {

    void execute(Keycloak keycloak);
}
