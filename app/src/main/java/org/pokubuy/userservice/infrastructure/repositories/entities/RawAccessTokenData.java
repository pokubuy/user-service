package org.pokubuy.userservice.infrastructure.repositories.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RawAccessTokenData(
    @JsonProperty("access_token")
    String accessToken
) {}
