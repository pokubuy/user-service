package org.pokubuy.userservice.application.configurationproperties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("keycloak")
@Component
@Data
public class KeycloakConfigurationProperties {

    private String url;
    private String realm;
    private String username;
    private String password;
    private String sslContext;
}
