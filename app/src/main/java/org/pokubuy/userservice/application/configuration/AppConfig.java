package org.pokubuy.userservice.application.configuration;

import org.pokubuy.userservice.domain.repositories.UserRepository;
import org.pokubuy.userservice.domain.services.UserService;
import org.pokubuy.userservice.domain.services.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class AppConfig {

    @Bean(name = "virtualThreadExecutorService")
    public ExecutorService virtualThreadExecutorService() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    @Bean
    public UserService userService(UserRepository userRepository) {
        return new UserServiceImpl(userRepository);
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }
}
