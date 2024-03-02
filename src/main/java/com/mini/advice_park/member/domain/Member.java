package com.mini.advice_park.member.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Entity
@Table(name = "members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private OAuth2Provider oAuth2Provider;

    private String providerId;
    private String email;
    private String password;
    private String name;
    private String firstName;
    private String lastName;
    private String nickname;
    private String image;
    private Boolean withdrawal;

    private Member(OAuth2Provider oAuth2Provider,
                   String providerId,
                   String email,
                   String password,
                   String name,
                   String firstName,
                   String lastName,
                   String nickname,
                   String image,
                   Role role) {
        this.oAuth2Provider = oAuth2Provider;
        this.providerId = providerId;
        this.email = email;
        this.password = password;
        this.name = name;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nickname = nickname;
        this.image = image;
        this.withdrawal = false;
        this.role = role;
    }

    public static Member of(OAuth2Provider oAuth2Provider,
                            String providerId,
                            String email,
                            String name,
                            String firstName,
                            String lastName,
                            String nickname,
                            String image) {
        return new Member(oAuth2Provider,
                providerId,
                email,
                null,
                name,
                firstName,
                lastName,
                nickname,
                image,
                Role.ROLE_USER);
    }

    public static Member of(String email,
                            String password,
                            String firstName,
                            String lastName,
                            String nickname,
                            String image,
                            PasswordEncoder encoder) {
        return new Member(OAuth2Provider.LOCAL,
                null,
                email,
                encoder.encode(password),
                firstName + " " + lastName,
                firstName,
                lastName,
                nickname,
                image,
                Role.ROLE_USER);
    }

    public static Member createDefaultAdmin(PasswordEncoder encoder) {
        return new Member(OAuth2Provider.LOCAL,
                null,
                "admin@admin.com",
                encoder.encode("admin"),
                "admin",
                null,
                null,
                "admin",
                null,
                Role.ROLE_ADMIN);
    }

    public void update(String name,
                       String firstName,
                       String lastName,
                       String nickname,
                       String image) {
        this.name = name;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nickname = nickname;
        this.image = image;
    }
}
