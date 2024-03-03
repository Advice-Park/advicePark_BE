package com.mini.advice_park;

import com.mini.advice_park.domain.user.entity.User;
import com.mini.advice_park.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@RequiredArgsConstructor
public class AdviceParkApplication {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

//    @PostConstruct
//    public void init() {
//        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
//    }

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
