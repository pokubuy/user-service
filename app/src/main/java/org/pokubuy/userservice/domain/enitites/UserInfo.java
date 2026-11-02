package org.pokubuy.userservice.domain.enitites;

import org.pokubuy.userservice.domain.enitites.enums.UserRole;

import java.util.List;

public record UserInfo(
    String username,
    String email,
    List<UserRole> roles
) {}
