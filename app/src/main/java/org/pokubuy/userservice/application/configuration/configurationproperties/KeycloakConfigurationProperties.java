package org.pokubuy.userservice.application.configuration.configurationproperties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("keycloak")
@Component
@Data
public final class KeycloakConfigurationProperties {

    private String url;
    private String targetRealmName;
    private String targetClientId;
    private Credentials adminCredentials;

    @Data
    public static class Credentials {

        private String realm;
        private String username;
        private String password;
        private String clientId;
    }
}
