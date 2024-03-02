package com.mini.advice_park;

import com.mini.advice_park.member.domain.Member;
import com.mini.advice_park.member.repository.MemberRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.TimeZone;

@SpringBootApplication
@RequiredArgsConstructor
public class AdviceParkApplication {

    private final MemberRepository memberRepository;
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
        if (memberRepository.findByEmail("admin@admin.com").isEmpty()) {
            memberRepository.save(Member.createDefaultAdmin(encoder));
        }
    }

}
