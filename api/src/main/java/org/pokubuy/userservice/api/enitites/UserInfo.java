package org.pokubuy.userservice.api.enitites;

import org.pokubuy.userservice.api.enitites.enums.UserRole;

import java.util.List;

public record UserInfo(
    String username,
    String email,
    List<UserRole> roles
) {}
