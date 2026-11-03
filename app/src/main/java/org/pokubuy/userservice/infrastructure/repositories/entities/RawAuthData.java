package org.pokubuy.userservice.infrastructure.repositories.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.pokubuy.userservice.domain.enitites.AuthData;

public record RawAuthData(
    @JsonProperty("access_token")
    String accessToken,

    @JsonProperty("refresh_token")
    String refreshToken
) {

    public AuthData toAuthData() {
        return new AuthData(accessToken, refreshToken);
    }
}
