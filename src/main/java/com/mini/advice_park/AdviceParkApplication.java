package com.mini.advice_park;

import com.mini.advice_park.domain.user.entity.User;
import com.mini.advice_park.domain.user.UserRepository;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableJpaAuditing
@SpringBootApplication
@RequiredArgsConstructor
@OpenAPIDefinition(servers = {@Server(url = "https://mooooonmin.site", description = "Https swagger")})
public class AdviceParkApplication {

    private final PasswordEncoder encoder;
    private final UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(AdviceParkApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        if (userRepository.findByEmail("admin@admin.com").isEmpty()) {
            userRepository.save(User.createDefaultAdmin(encoder));
        }
    }

}
