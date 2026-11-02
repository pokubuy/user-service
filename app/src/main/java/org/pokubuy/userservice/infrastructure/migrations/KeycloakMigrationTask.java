package org.pokubuy.userservice.infrastructure.migrations;

import org.keycloak.admin.client.Keycloak;

@FunctionalInterface
public interface KeycloakMigrationTask {

    void execute(Keycloak keycloak, String targetRealmName);
}
