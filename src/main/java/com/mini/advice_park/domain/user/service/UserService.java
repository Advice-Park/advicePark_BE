package com.mini.advice_park.domain.user.service;

import com.mini.advice_park.domain.user.UserRepository;
import com.mini.advice_park.domain.user.dto.SignUpRequest;
import com.mini.advice_park.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입 서비스
     */
    @Transactional
    public void signUp(SignUpRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();
        String firstName = request.getFirstName();
        String lastName = request.getLastName();
        String nickname = request.getNickname();
        String image = request.getImage();

        User user = User.of(email, password, firstName, lastName, nickname, image, passwordEncoder);

        userRepository.save(user);
    }

}
