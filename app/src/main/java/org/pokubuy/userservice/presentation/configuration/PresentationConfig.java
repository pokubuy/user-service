package org.pokubuy.userservice.presentation.configuration;

import org.pokubuy.userservice.api.mappers.AuthDataMapper;
import org.pokubuy.userservice.api.mappers.UserInfoMapper;
import org.pokubuy.userservice.api.mappers.UserRoleMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PresentationConfig {

    @Bean
    public UserRoleMapper userRoleMapper() {
        return new UserRoleMapper();
    }

    @Bean
    public UserInfoMapper userInfoMapper(UserRoleMapper userRoleMapper) {
        return new UserInfoMapper(userRoleMapper);
    }

    @Bean
    public AuthDataMapper authDataMapper() {
        return new AuthDataMapper();
    }
}
